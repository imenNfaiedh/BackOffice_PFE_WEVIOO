package com.example.kafka_service.dto;

import com.example.kafka_service.enumeration.TransactionStatus;
import com.example.kafka_service.enumeration.TypeBankAccount;
import com.example.kafka_service.enumeration.TypeTransaction;
import com.example.kafka_service.enumeration.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEnrichedDto implements Serializable {

    private Long transactionId;
    private double amount;
    private String currency;
    private String country;
    private Date transactionDate;

    @Enumerated(EnumType.STRING)
    private TypeTransaction typeTransaction;

    @Enumerated (EnumType.STRING)
    private TransactionStatus transactionStatus;

    private Long bankAccountId;
    private Long accountNumber;
    private Date openingDate;
    private double balance;
    @Enumerated(EnumType.STRING)
    private TypeBankAccount typeBankAccount;

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String tel;
    private Boolean suspicious_activity;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}