package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.entity.BankAccount;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.entity.User;
import com.example.transaction_service.enumeration.TransactionStatus;
import com.example.transaction_service.exception.NotFoundException;
import com.example.transaction_service.repository.IBankAccountRepository;
import com.example.transaction_service.repository.ITransactionRepository;
import com.example.transaction_service.repository.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import java.math.BigInteger;
import java.util.Base64;
import java.util.Map;

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
    @Autowired
    private IBankAccountRepository bankAccountRepository;


    public void startProcess(Map<String, Object> data) {
        try {
            log.info("Processus déclenché avec données : {}", data);

            Integer transactionIdInteger = (Integer) data.get("fds004_transaction_id");
            Long transactionId = transactionIdInteger != null ? transactionIdInteger.longValue() : null;
            //get amount & decode du base 16->2
            Object amountRaw = data.get("fds004_amount");
            BigDecimal amount = decodeDebeziumDecimal(amountRaw);

            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new NotFoundException("Transaction not found " + transactionId));



            User user = transaction.getBankAccount().getUser();
            if (user == null) {
                log.error("user not found ");
                throw new NotFoundException("user not found");
            }
           if(transaction.getIsSendNotification() != null &&  transaction.getIsSendNotification().equals(Boolean.TRUE)) {

               Long userId = user.getUserId();

               boolean isFraudulent = false;
               List<String> reasons = new ArrayList<>();

               if (isHighAmount(amount, reasons)) {
                   isFraudulent = true;
               }

               // Période à analyser : les 10 dernières minutes
               Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
               List<Transaction> recentTransactions = transactionRepository
                       .findByBankAccount_User_UserIdAndTransactionDateAfter(userId, tenMinutesAgo);

               if (isHighFrequency(recentTransactions, reasons)) {
                   isFraudulent = true;
               }

               if (isMultipleCountries(recentTransactions, reasons)) {
                   isFraudulent = true;
               }

               if (isFraudulent) {
                   transaction.setTransactionStatus(TransactionStatus.SUSPICIOUS);
                   user.setSuspicious_activity(true);
                   userRepository.save(user);

                   BankAccount bankAccount = transaction.getBankAccount();


                   Integer fraudCount = bankAccount.getFraudCount() != null ? bankAccount.getFraudCount() : 0;
                   fraudCount++;
                   bankAccount.setFraudCount(fraudCount);

                   if (fraudCount >= 3) {
                       bankAccount.setIsBlocked(true);
                       log.warn("Le compte bancaire {} est bloqué après {} fraudes détectées", bankAccount.getBankAccountId(), fraudCount);
                   }

                   bankAccountRepository.save(bankAccount);



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
               } else {
                   transaction.setTransactionStatus(TransactionStatus.VALID);
                   log.info("Aucune fraude détectée pour cette transaction, statut mis à VALIDATED.");
               }

               transaction.setIsSendNotification(Boolean.FALSE);
               transactionRepository.save(transaction);

           }
        } catch (Exception e) {
            log.error("Erreur dans le processus de détection de fraude : ", e);
        }
    }


    // Règle 1
    private boolean isHighAmount(BigDecimal amount, List<String> reasons) {
        if (amount != null && amount.compareTo(BigDecimal.valueOf(3000)) > 0) {
            reasons.add("Montant supérieur à 3000");
            return true;
        }

        return false;
    }

    // Règle 2
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


    // decode amount base decimal=> base 2
    public BigDecimal decodeDebeziumDecimal(Object debeziumDecimal) {
        if (debeziumDecimal instanceof Map) {
            Map<String, Object> decimalMap = (Map<String, Object>) debeziumDecimal;
            String base64Value = (String) decimalMap.get("value");
            Integer scale = (Integer) decimalMap.get("scale");

            byte[] bytes = Base64.getDecoder().decode(base64Value);
            BigInteger unscaledValue = new BigInteger(bytes);
            return new BigDecimal(unscaledValue, scale);
        } else if (debeziumDecimal instanceof String) {
            // Parfois c’est juste un string base64 sans scale, tu peux adapter selon ce que tu reçois
            String base64Value = (String) debeziumDecimal;
            byte[] bytes = Base64.getDecoder().decode(base64Value);
            BigInteger unscaledValue = new BigInteger(bytes);
            // Par défaut, scale = 0 si pas précisé
            return new BigDecimal(unscaledValue, 0);
        } else if (debeziumDecimal instanceof Number) {
            // Si Debezium renvoie un nombre simple (double, int, etc)
            return new BigDecimal(debeziumDecimal.toString());
        }
        throw new IllegalArgumentException("Format decimal inconnu : " + debeziumDecimal);
    }



}