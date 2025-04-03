package com.example.kafka_service.consumer;


import com.example.kafka_service.dto.TransactionEnrichedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class KafkaConsumer {

//    @KafkaListener(topics = "any-topic-name", groupId = "myGrp")
//    public void consumeMessage(String message) {
//        System.out.println("Received message: " + message);
//    }

    @KafkaListener(topics = "enriched-transactions", groupId = "myGrp")
    public void consumeTransaction(String message) {
        try {
            // Convertir le JSON string en objet TransactionEnrichedDto
            ObjectMapper objectMapper = new ObjectMapper();
            TransactionEnrichedDto transaction = objectMapper.readValue(message, TransactionEnrichedDto.class);

            log.info("Received Transaction: " + transaction);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}

