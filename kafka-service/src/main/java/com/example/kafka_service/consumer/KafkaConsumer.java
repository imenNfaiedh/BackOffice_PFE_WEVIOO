package com.example.kafka_service.consumer;


import com.example.kafka_service.dto.TransactionEnrichedDto;
import com.example.kafka_service.payload.Student;
import com.example.kafka_service.producer.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
//@Slf4j
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

            System.out.println("Received Transaction: " + transaction);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}

