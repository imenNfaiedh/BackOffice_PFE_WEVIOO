package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.TransactionEnrichedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class KafkaProducer {

    private static final String FRAUD_DETECTION_TOPIC = "fraud-detection-results";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;  // Modifier pour envoyer une chaîne (String)

    public void sendFraudDetectionResult(String resultJson)  {
        try {
            kafkaTemplate.send(FRAUD_DETECTION_TOPIC, resultJson);
            log.info("Résultat envoyé dans Kafka : " + resultJson);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi du résultat dans Kafka : " + e.getMessage());
        }
    }
}




