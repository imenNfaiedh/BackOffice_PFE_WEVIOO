package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.entity.User;
import com.example.transaction_service.exception.NotFoundException;
import com.example.transaction_service.repository.ITransactionRepository;
import com.example.transaction_service.repository.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcessService {
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
   private ITransactionRepository transactionRepository;
    @Autowired
    private IUserRepository userRepository;

    public void startProcess(Map<String, Object> data)  {
        try {
            log.info(" Processus déclenché avec données : {}", data);

            Integer transactionIdInteger = (Integer) data.get("fds004_transaction_id");
            Long transactionId = transactionIdInteger != null ? transactionIdInteger.longValue() : null;
            Double amount = (Double) data.get("fds004_amount");

            Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

            User user = transaction.getBankAccount().getUser();
            Long userId = user.getUserId();


            // Règle 1
            boolean isFraudulent = false;
            String reason = "Transaction valide";
            if (amount > 3000) {
                isFraudulent = true;
                reason = "Montant supérieur à 3000";
            }
            // Règle 2 : Détection des transactions entre différents pays en une période de temps courte
            else if ((!isFraudulent)) {

                    // Période de 10 minutes avant la transaction actuelle
                    Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000);

                    // Rechercher les transactions récentes de l'utilisateur
                    List<Transaction> recentCountryTransactions = transactionRepository
                            .findByBankAccount_User_UserIdAndTransactionDateAfter(userId, tenMinutesAgo);

                    // Extraire les pays des transactions récentes
                    Set<String> countries = recentCountryTransactions.stream()
                            .map(Transaction::getCountry)
                            .collect(Collectors.toSet());

                    // Si l'utilisateur a effectué des transactions dans plus d'un pays dans les 10 dernières minutes
                    if (countries.size() > 1) {
                        isFraudulent = true;
                        reason = "Transactions dans plusieurs pays sur une courte période";
                    }
                }





            else   {
                log.info("Transaction non frauduleuse, pas d'envoi dans Kafka.");
                return;
            }
            user.setSuspicious_activity(true);
            userRepository.save(user);

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", user.getUserId());
            userMap.put("firstName", user.getFirstName());
            userMap.put("lastName", user.getLastName());
            userMap.put("email", user.getEmail());
            userMap.put("tel", user.getTel());
            userMap.put("suspiciousActivity", user.getSuspicious_activity());

            // Construire le résultat
            Map<String, Object> fraudResult = new HashMap<>();

            fraudResult.put("transactionId", transaction.getTransactionId());
            fraudResult.put("amount", transaction.getAmount());
            fraudResult.put("country", transaction.getCountry());
            fraudResult.put("user", userMap);
            fraudResult.put("reason", reason);


            String fraudResultJson = objectMapper.writeValueAsString(fraudResult);
            // Envoyer le résultat via Kafka Producer
            kafkaProducer.sendFraudDetectionResult(fraudResultJson);

        } catch (Exception e) {
            log.error("Erreur lors du traitement des données : ", e);
        }
    }
}