package com.example.notification_service.consumer;

import com.example.notification_service.entities.EmailDetails;
import com.example.notification_service.service.EmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaFraudListener {
    @Autowired
    private EmailService emailService;
    @KafkaListener(topics = "fraud-detection-results", groupId = "notification-group")
    public void listenFraudDetection(String message) {
        log.info("Message de fraude reçu : {}", message);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(message);

            JsonNode user = jsonNode.path("user");
            String email = user.path("email").asText();
            String firstName = user.path("firstName").asText();
            Double amount = jsonNode.path("amount").asDouble();
            String country = jsonNode.path("country").asText();
            String reason = jsonNode.path("reason").asText();

            String subject = "🚨 Fraude détectée sur votre compte";
            String body = "Bonjour " + firstName + ",\n\n" +
                    "Une activité suspecte a été détectée sur votre compte bancaire.\n" +
                    "Voici les détails :\n\n" +
                    "Montant : " + amount + " €\n" +
                    "Pays : " + country + "\n" +
                    "Raison : " + reason + "\n\n" +
                    "Merci de vérifier cette activité ou contacter notre service client.\n\n" +
                    "Ceci est un message automatique.";

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(email);
            emailDetails.setSubject(subject);
            emailDetails.setMsgBody(body);

            String result = emailService.sendSimpleMail(emailDetails);
            log.info("Résultat de l'envoi d'email : {}", result);

        } catch (Exception e) {
            log.error("Erreur lors du traitement du message Kafka pour l'email : ", e);
        }
    }
}

