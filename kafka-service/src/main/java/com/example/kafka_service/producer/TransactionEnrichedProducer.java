package com.example.kafka_service.producer;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.example.transaction_service.dto.TransactionEnrichedDto;

@Service
public class TransactionEnrichedProducer {
    @Autowired
    private KafkaTemplate<String, TransactionEnrichedDto> kafkaTemplate;

    private final String TOPIC = "enriched-transactions";


    public void sendTransactionToKafka(TransactionEnrichedDto enrichedDto) {
        kafkaTemplate.send(TOPIC, enrichedDto);
    }
}