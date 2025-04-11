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

import java.util.HashMap;
import java.util.Map;

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
            // Règle 2


            if (!isFraudulent) {
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


            String fraudResultJson = objectMapper.writeValueAsString(fraudResult);
            // Envoyer le résultat via Kafka Producer
            kafkaProducer.sendFraudDetectionResult(fraudResultJson);

        } catch (Exception e) {
            log.error("Erreur lors du traitement des données : ", e);
        }
    }
}