package com.example.kafka_service.consumer;

import com.example.kafka_service.dto.FraudAlertDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FraudAlertConsumer {
    @KafkaListener(topics = "suspicious-users", groupId = "myGrp")
    public void listen(FraudAlertDto alert) {
        log.info(" Fraude détectée pour l'utilisateur : " + alert.getUserId() +
                " | Montant : " + alert.getAmount());
    }
}
