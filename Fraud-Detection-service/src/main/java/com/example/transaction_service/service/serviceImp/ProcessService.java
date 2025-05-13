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
                    .orElseThrow(() -> new NotFoundException("Transaction not found" +transactionId));

            User user = transaction.getBankAccount().getUser();
            if (user==null){
                log.error("user not found ");
                throw new NotFoundException("user not found");
            }
            Long userId = user.getUserId();

            boolean isFraudulent = false;
            List<String> reasons = new ArrayList<>();

            if (isHighAmount(amount, reasons)) {
                isFraudulent = true;
            }

            //  Période à analyser : les 10 dernières minutes
            Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
            List<Transaction> recentTransactions = transactionRepository
                    .findByBankAccount_User_UserIdAndTransactionDateAfter(userId, tenMinutesAgo);

            //  Règle 2
            if (isHighFrequency(recentTransactions, reasons)) {
                isFraudulent = true;
            }

            //  Règle 3
            if (isMultipleCountries(recentTransactions, reasons)) {
                isFraudulent = true;
            }


            if (!isFraudulent) {
                log.info("Aucune fraude détectée pour cette transaction.");
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

            Map<String, Object> fraudResult = new HashMap<>();
            fraudResult.put("transactionId", transaction.getTransactionId());
            fraudResult.put("amount", transaction.getAmount());
            fraudResult.put("country", transaction.getCountry());
            fraudResult.put("user", userMap);
            fraudResult.put("reason", String.join(" & ", reasons));


            String fraudResultJson = objectMapper.writeValueAsString(fraudResult);
            kafkaProducer.sendFraudDetectionResult(fraudResultJson);
            log.info("Transaction frauduleuse détectée et envoyée : {}", fraudResultJson);

        } catch (Exception e) {
            log.error("Erreur dans le processus de détection de fraude : ", e);
        }
    }

    // Règle 1
    private boolean isHighAmount(Double amount, List<String> reasons) {
        if (amount != null && amount > 3000) {
            reasons.add("Montant supérieur à 3000");
            return true;
        }
        return false;
    }

    //  Règle 2
    private boolean isHighFrequency(List<Transaction> recentTransactions, List<String> reasons) {
        if (recentTransactions.size() > 5) {
            reasons.add("Trop de transactions en moins de 10 minutes");
            return true;
        }
        return false;
    }

    // Règle 3
    private boolean isMultipleCountries(List<Transaction> recentTransactions, List<String> reasons) {
        Set<String> countries = recentTransactions.stream()
                .map(Transaction::getCountry)
                .collect(Collectors.toSet());

        if (countries.size() > 1) {
            reasons.add("Transactions dans plusieurs pays sur une courte période");
            return true;
        }
        return false;
    }
}
