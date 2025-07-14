package com.example.notification_service.consumer;

import com.example.notification_service.entities.EmailDetails;
import com.example.notification_service.service.EmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class KafkaFraudListener {
    @Autowired
    private EmailService emailService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "fraud-detection-results", groupId = "notification-group")
    public void listenFraudDetection(String message) {
        log.info("Message de fraude reçu : {}", message);

        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(message);
            //Extraction des informations depuis le message
            JsonNode user = jsonNode.path("user");


            String email = user.path("email").asText();
            String firstName = user.path("firstName").asText();
            String lastName = user.path("lastName").asText();

            Double amount = jsonNode.path("amount").asDouble();
            String country = jsonNode.path("country").asText();
            String reason = jsonNode.path("reason").asText();


            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(email);
            emailDetails.setSubject("🚨 Alerte de fraude détectée");

            //create model utilisé dans le template HTML de l’e-mail.
            Map<String, Object> model = new HashMap<>();
            model.put("lastName", lastName);
            model.put("firstName", firstName);

            model.put("amount", amount);
            model.put("country", country);
            model.put("reason", reason);

            messagingTemplate.convertAndSend("/topic/fraud-alerts", model);
            log.info("📢 Notification WebSocket envoyée : {}", model);

        //     Envoi de l'e-mail HTML
            String result = emailService.sendHtmlMailWithTemplate(emailDetails, model,"fraud-alert");
            log.info("📬 Résultat de l'envoi de l'e-mail : {}", result);
            // envoyer via websocket


        } catch (Exception e) {
            log.error("Erreur lors de l'envoi du mail HTML : {}", e.getMessage(), e);
        }

    }


}