package com.example.transaction_service.listener;

import com.example.transaction_service.service.serviceImp.KafkaProducer;
import com.example.transaction_service.service.serviceImp.ProcessService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class TransactionChangeListener {



    @Autowired
    private ProcessService processService;


    @KafkaListener(topics = "dbserver1.public.fds004t_transaction")
    public void ecouter(String message) {
        log.info(" Événement reçu : " + message);

        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode json = mapper.readTree(message);

            // Extraire les données après la modification
            JsonNode afterData = json.path("payload").path("after");
            if (afterData != null) {
                Map<String, Object> transactionDto = mapper.convertValue(afterData, Map.class);
                log.info("Traitement des données après modification : {}", transactionDto);

                processService.startProcess(transactionDto);
            } else {
                log.warn("Données manquantes dans l'événement : {}", message);
            }

        } catch (Exception e) {
            log.error("Erreur lors du traitement du message : ", e);
        }
    }
}