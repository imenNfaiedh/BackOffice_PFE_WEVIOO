package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.TransactionEnrichedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final String TOPIC_NAME = "enriched-transactions"; // Replace with your desired topic name

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendTransaction(TransactionEnrichedDto transaction) {
        try {
            // Convertir l'objet en JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String transactionJson = objectMapper.writeValueAsString(transaction);

            // Envoyer le message
            kafkaTemplate.send("enriched-transactions", transactionJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }}

//    private final KafkaTemplate<String, String> kafkaTemplate;
//public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
//    this.kafkaTemplate = kafkaTemplate;
//}


//    public void sendMessage(String message) {
//        kafkaTemplate.send(TOPIC_NAME, message);
//        System.out.println("Message " + message +
//                " has been sucessfully sent to the topic: " + TOPIC_NAME);
//    }





