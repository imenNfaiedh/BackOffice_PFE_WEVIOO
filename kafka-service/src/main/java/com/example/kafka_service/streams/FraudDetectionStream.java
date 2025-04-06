package com.example.kafka_service.streams;


import com.example.kafka_service.dto.TransactionEnrichedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Properties;


@Service
@Slf4j
public class FraudDetectionStream {
    private static final String INPUT_TOPIC = "enriched-transactions";
    private static final String OUTPUT_TOPIC = "suspicious-users";

    @PostConstruct
    public void init() {
        startStreamProcessing();
    }

    public void startStreamProcessing() {
        StreamsBuilder builder = new StreamsBuilder();

        // Consommer les données en tant que String
        KStream<String, String> rawStream = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        // Désérialisation  des données en String
        KStream<String, TransactionEnrichedDto> parsedStream = rawStream
                .mapValues(value -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(value, TransactionEnrichedDto.class); // Désérialisation en DTO
                    } catch (JsonProcessingException e) {
                        log.error("Erreur de désérialisation de la transaction", e);
                        return null;
                    }
                })
                .filter((key, value) -> value != null)
                .selectKey((key, value) -> value.getUserId().toString());


        // Regroupement par userId
        KGroupedStream<String, TransactionEnrichedDto> groupedByUser = parsedStream
                .groupByKey(Grouped.with(Serdes.String(), new JsonSerde<>(TransactionEnrichedDto.class)));

        // Fenêtre glissante d'une heure
        TimeWindows timeWindow = TimeWindows.of(Duration.ofHours(1)).advanceBy(Duration.ofMinutes(15));
        KTable<Windowed<String>, Double> windowedTransactions = groupedByUser
                .windowedBy(timeWindow)
                .aggregate(
                        () -> 0.0,
                        (key, transaction, aggregate) -> aggregate + transaction.getAmount(), // Accès au montant de la transaction
                        Materialized.<String, Double, WindowStore<Bytes, byte[]>>as("user-transaction-aggregates")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Double())
                );

        // Filtrer les utilisateurs suspects
        KStream<String, Double> suspiciousUsers = windowedTransactions
                .toStream()
                .filter((windowedKey, totalAmount) -> totalAmount > 30000)
                .map((windowedKey, totalAmount) -> KeyValue.pair(windowedKey.key(), totalAmount));

        // Produire des alertes sous forme de String (au lieu de JSON)
        suspiciousUsers.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Double()));

        // Lancer le stream
        KafkaStreams streams = new KafkaStreams(builder.build(), getStreamsConfig());
        streams.start();
    }

    private Properties getStreamsConfig() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "fraud-detection-processor");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass()); // Utilisation du String Serde
        return props;
    }
}

