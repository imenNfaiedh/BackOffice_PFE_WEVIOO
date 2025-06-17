package com.example.notification_service.consumer;

import com.example.notification_service.entities.EmailDetails;
import com.example.notification_service.service.EmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaClaimResponseListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "claim-response-topic", groupId = "notification-group")
    public void listenClaimResponse(String message) {
        log.info("📥 Message Kafka reçu : {}", message);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            String email = jsonNode.path("email").asText();
            String firstName = jsonNode.path("firstName").asText();
            String lastName = jsonNode.path("lastName").asText(); // ADD
            String subject = jsonNode.path("subject").asText();
            String content = jsonNode.path("content").asText();

            Map<String, Object> model = new HashMap<>();
            model.put("firstName", firstName);
            model.put("lastName", lastName); // ADD
            model.put("content", content);

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(email);
            emailDetails.setSubject(subject);

            String result = emailService.sendHtmlMailWithTemplate(emailDetails, model, "claim-response");
            log.info("📧 Résultat envoi e-mail : {}", result);

            messagingTemplate.convertAndSend("/topic/claim-response", model);
            log.info("🔔 WebSocket notification envoyée");
        } catch (Exception e) {
            log.error("❌ Erreur lors du traitement de la réponse Kafka", e);
        }
    }
}
