package com.example.transaction_service.service.serviceImp;


import com.example.transaction_service.dto.TransactionEnrichedDto;
import com.example.transaction_service.entity.BankAccount;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.entity.User;

import com.example.transaction_service.repository.ITransactionRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class TransactionServiceEnriched {
    @Autowired
    private ITransactionRepository transactionRepository;

     @Autowired
     KafkaProducer kafkaProducer;

    private final String TOPIC = "enriched-transactions";

    public TransactionEnrichedDto enrichTransaction(Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));


        BankAccount bankAccount = transaction.getBankAccount();
        if (bankAccount != null) {
            Long bankAccountId = bankAccount.getBankAccountId();
            log.info("ID du compte bancaire : " + bankAccountId);
        } else {
            throw new RuntimeException("Le compte bancaire est null dans la transaction.");
        }


        User user = bankAccount.getUser();
        // Créer un DTO enrichi
        TransactionEnrichedDto enrichedDto = new TransactionEnrichedDto();
        enrichedDto.setTransactionId(transaction.getTransactionId());
        enrichedDto.setAmount(transaction.getAmount());
        enrichedDto.setCurrency(transaction.getCurrency());
        enrichedDto.setCountry(transaction.getCountry());
        enrichedDto.setTransactionDate(transaction.getTransactionDate());
        enrichedDto.setTypeTransaction(transaction.getTypeTransaction());
        enrichedDto.setTransactionStatus(transaction.getTransactionStatus());

        enrichedDto.setBankAccountId(bankAccount.getBankAccountId());
        enrichedDto.setAccountNumber(bankAccount.getAccountNumber());
        enrichedDto.setOpeningDate(bankAccount.getOpeningDate());
        enrichedDto.setBalance(bankAccount.getBalance());
        enrichedDto.setTypeBankAccount(bankAccount.getTypeBankAccount());
        if(Objects.isNull(user)){
            throw  new NotFoundException(" user not found");
        }
        enrichedDto.setUserId(user.getUserId());
        enrichedDto.setFirstName(user.getFirstName());
        enrichedDto.setLastName(user.getLastName());
        enrichedDto.setEmail(user.getEmail());
        enrichedDto.setTel(user.getTel());
        enrichedDto.setSuspicious_activity(user.getSuspicious_activity());
        enrichedDto.setRole(user.getRole());

        // Envoi de la transaction enrichie à Kafka
//        kafkaProducer.sendMessage("hello");
        kafkaProducer.sendTransaction( enrichedDto);
        log.info("Transaction enrichie envoyée à Kafka : {}", enrichedDto);

        return enrichedDto;
    }
}


