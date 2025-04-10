package com.example.transaction_service.service.serviceImp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ProcessService {
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ObjectMapper objectMapper;

    public void startProcess(Map<String, Object> data)  {
        try {
            log.info(" Processus déclenché avec données : {}", data);

            // Extraire les champs nécessaires
            Integer transactionIdInteger = (Integer) data.get("fds004_transaction_id");
            Long transactionId = transactionIdInteger != null ? transactionIdInteger.longValue() : null;
            Double amount = (Double) data.get("fds004_amount");

            // Règle de détection de fraude basée sur le montant
            boolean isFraudulent = false;
            String reason = "Transaction valide";

            if (amount > 3000) {
                isFraudulent = true;
                reason = "Montant supérieur à 3000";
            }

            if (!isFraudulent) {
                log.info("Transaction non frauduleuse, pas d'envoi dans Kafka.");
                return;
            }

            // Construire le résultat
            Map<String, Object> fraudResult = new HashMap<>();
            fraudResult.put("transaction_id", transactionId);
            fraudResult.put("is_fraudulent", isFraudulent);
            fraudResult.put("reason", reason);


            String fraudResultJson = objectMapper.writeValueAsString(fraudResult);
            // Envoyer le résultat via Kafka Producer
            kafkaProducer.sendFraudDetectionResult(fraudResultJson);

        } catch (Exception e) {
            log.error("Erreur lors du traitement des données : ", e);
        }
    }
}