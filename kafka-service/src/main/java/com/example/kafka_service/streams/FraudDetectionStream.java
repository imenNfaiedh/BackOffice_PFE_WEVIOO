package com.example.kafka_service.streams;


import com.example.kafka_service.dto.FraudAlertDto;
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

        // 1. Lire les messages JSON sous forme de String
        KStream<String, String> rawStream = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        // 2. Désérialiser le JSON en DTO
        KStream<String, TransactionEnrichedDto> parsedStream = rawStream
                .mapValues(value -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(value, TransactionEnrichedDto.class);
                    } catch (JsonProcessingException e) {
                        log.error("Erreur de désérialisation de la transaction", e);
                        return null;
                    }
                })
                .filter((key, value) -> value != null)
                .selectKey((key, value) -> value.getUserId().toString());

        // 3. Grouper par userId
        KGroupedStream<String, TransactionEnrichedDto> groupedByUser = parsedStream
                .groupByKey(Grouped.with(Serdes.String(), new JsonSerde<>(TransactionEnrichedDto.class)));

        // 4. Fenêtrage glissant
        TimeWindows timeWindow = TimeWindows.of(Duration.ofHours(1)).advanceBy(Duration.ofMinutes(15));

        // 5. Agrégation du montant des transactions
        KTable<Windowed<String>, Double> windowedTransactions = groupedByUser
                .windowedBy(timeWindow)
                .aggregate(
                        () -> 0.0,
                        (key, transaction, aggregate) -> aggregate + transaction.getAmount(),
                        Materialized.<String, Double, WindowStore<Bytes, byte[]>>as("user-transaction-aggregates")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Double())
                );

        // 6. Filtrer les utilisateurs suspects (montant > 30 000)
        KStream<String, Double> suspiciousUsers = windowedTransactions
                .toStream()
                .filter((windowedKey, totalAmount) -> totalAmount > 30000)
                .map((windowedKey, totalAmount) -> KeyValue.pair(windowedKey.key(), totalAmount));

        // 7. Transformer en DTO d'alerte
        KStream<String, FraudAlertDto> alertsStream = suspiciousUsers.mapValues((userId, amount) -> {
            FraudAlertDto alert = new FraudAlertDto();
            alert.setUserId(Long.parseLong(userId));
            alert.setAmount(amount);
            alert.setTimestamp(System.currentTimeMillis()); // Timestamp actuel
            alert.setSuspiciousActivity(true); // Puisqu'on a dépassé le seuil
            return alert;
        });

        // 8. Écrire dans le topic "suspicious-users"
        alertsStream.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), new JsonSerde<>(FraudAlertDto.class)));

        // 9. Config et démarrage
        KafkaStreams streams = new KafkaStreams(builder.build(), getStreamsConfig());
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private Properties getStreamsConfig() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "fraud-detection-processor");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return props;
    }
}

