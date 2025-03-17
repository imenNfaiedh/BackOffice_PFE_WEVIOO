package com.example.kafka_service.consumer;

import com.example.kafka_service.feign.TransactionEnrichedClient;
import com.example.transaction_service.dto.TransactionEnrichedDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionEnrichedConsumer {
    @Autowired
    private TransactionEnrichedClient transactionEnrichedClient;

    @KafkaListener(topics = "enriched-transactions", groupId = "myGrp")
    public void consume(TransactionEnrichedDto enrichedDto) {
        System.out.println("Transaction enrichie reçue de Kafka : " + enrichedDto);

        // Appel du service d'enrichissement
        TransactionEnrichedDto enrichedTransaction = transactionEnrichedClient.enrichTransaction(enrichedDto.getTransactionId());

        System.out.println("Transaction après enrichissement : " + enrichedTransaction);
    }
    }

