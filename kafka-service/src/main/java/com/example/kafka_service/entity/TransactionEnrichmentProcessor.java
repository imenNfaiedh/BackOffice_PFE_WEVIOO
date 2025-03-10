package com.example.kafka_service.entity;

import com.example.kafka_service.config.KafkaStreamsConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.stereotype.Component;


import java.util.Properties;

@Component
public class TransactionEnrichmentProcessor {

    private final Properties kafkaStreamsProperties;

    public TransactionEnrichmentProcessor(KafkaStreamsConfig config) {
        this.kafkaStreamsProperties = config.kafkaStreamsProperties();
    }

    public void start() {
        StreamsBuilder builder = new StreamsBuilder();

        // Lire le flux de transactions
        KStream<Long, String> transactionStream = builder.stream(
                "dbserver1.public.fds004t_transaction",
                Consumed.with(Serdes.Long(), Serdes.String())
        );


        // Lire la table des comptes bancaires
        KTable<Long, String> bankAccountTable = builder.table(
                "dbserver1.public.fds002t_bank_account",
                Consumed.with(Serdes.Long(), Serdes.String())
        ).filter((key, value) -> value != null);

        // Lire la table des utilisateurs
        KTable<Long, String> userTable = builder.table(
                "dbserver1.public.fds005t_user",
                Consumed.with(Serdes.Long(), Serdes.String())
        ).filter((key, value) -> value != null);



        // Première jointure : Transaction + Compte bancaire
        KStream<Long, String> enrichedWithBank = transactionStream
                .filter((key, value) -> value != null && JsonUtil.extractLongField(value, "bank_Account_id") != null)
                .selectKey((key, value) -> JsonUtil.extractLongField(value, "bank_Account_id"))
                .join(bankAccountTable,
                        (txJson, bankJson) -> JsonUtil.mergeJsonObjects(txJson, bankJson),
                        Joined.with(Serdes.Long(), Serdes.String(), Serdes.String())
                );

        // Seconde jointure : Résultat précédent + Utilisateur
        KStream<Long, String> enrichedStream = enrichedWithBank
                .filter((key, value) -> value != null && JsonUtil.extractLongField(value, "user_id") != null)
                .selectKey((key, value) -> JsonUtil.extractLongField(value, "user_id"))
                .join(userTable,
                        (enrichedJson, userJson) -> JsonUtil.mergeJsonObjects(enrichedJson, userJson),
                        Joined.with(Serdes.Long(), Serdes.String(), Serdes.String())
                );

        // Envoi vers le topic final
        enrichedStream.to("enriched-transactions", Produced.with(Serdes.Long(), Serdes.String()));

        // Configuration et démarrage
        KafkaStreams streams = new KafkaStreams(builder.build(), kafkaStreamsProperties);
        streams.start();

        // Gestion de l'arrêt propre
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}