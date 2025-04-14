package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.entity.User;
import com.example.transaction_service.exception.NotFoundException;
import com.example.transaction_service.repository.ITransactionRepository;
import com.example.transaction_service.repository.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ProcessService {
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ITransactionRepository transactionRepository;
    @Autowired
    private IUserRepository userRepository;

    public void startProcess(Map<String, Object> data) {
        try {
            log.info("Processus déclenché avec données : {}", data);

            Integer transactionIdInteger = (Integer) data.get("fds004_transaction_id");
            Long transactionId = transactionIdInteger != null ? transactionIdInteger.longValue() : null;
            Double amount = (Double) data.get("fds004_amount");

            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new NotFoundException("Transaction not found"));

            User user = transaction.getBankAccount().getUser();
            Long userId = user.getUserId();

            boolean isFraudulent = false;
            List<String> reasons = new ArrayList<>();

            // Règle 1 : montant > 3000
            if (amount > 3000) {
                isFraudulent = true;
                reasons.add("Montant supérieur à 3000");
            }

            // Règle 2 : transactions dans plusieurs pays en moins de 10 minutes
            Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
            List<Transaction> recentTransactions = transactionRepository
                    .findByBankAccount_User_UserIdAndTransactionDateAfter(userId, tenMinutesAgo);

            Set<String> countries = recentTransactions.stream()
                    .map(Transaction::getCountry)
                    .collect(Collectors.toSet());

            if (countries.size() > 1) {
                isFraudulent = true;
                reasons.add("Transactions dans plusieurs pays sur une courte période");
            }

            // Si aucune règle n'est déclenchée, pas besoin d'envoyer
            if (!isFraudulent) {
                log.info("Transaction non frauduleuse, pas d'envoi dans Kafka.");
                return;
            }

            // Marquer l'utilisateur comme suspect
            user.setSuspicious_activity(true);
            userRepository.save(user);

            // Construire le map utilisateur
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", user.getUserId());
            userMap.put("firstName", user.getFirstName());
            userMap.put("lastName", user.getLastName());
            userMap.put("email", user.getEmail());
            userMap.put("tel", user.getTel());
            userMap.put("suspiciousActivity", user.getSuspicious_activity());

            // Construire le résultat à envoyer
            Map<String, Object> fraudResult = new HashMap<>();
            fraudResult.put("transactionId", transaction.getTransactionId());
            fraudResult.put("amount", transaction.getAmount());
            fraudResult.put("country", transaction.getCountry());
            fraudResult.put("user", userMap);
            fraudResult.put("reason", String.join(" & ", reasons)); // On concatène les raisons

            // Convertir en JSON et envoyer via Kafka
            String fraudResultJson = objectMapper.writeValueAsString(fraudResult);
            kafkaProducer.sendFraudDetectionResult(fraudResultJson);

        } catch (Exception e) {
            log.error("Erreur lors du traitement des données : ", e);
        }
    }}
