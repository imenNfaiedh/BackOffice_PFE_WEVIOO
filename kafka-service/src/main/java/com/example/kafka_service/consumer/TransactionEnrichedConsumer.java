package com.example.kafka_service.consumer;

import com.example.transaction_service.dto.TransactionEnrichedDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionEnrichedConsumer {

    @KafkaListener(topics = "transaction-enriched", groupId = "myGrp")
    public void consume(TransactionEnrichedDto enrichedDto) {

        System.out.println("Transaction enrichie reçue : " + enrichedDto);

    }
    }

