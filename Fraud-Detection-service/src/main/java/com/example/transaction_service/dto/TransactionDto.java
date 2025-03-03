package com.example.transaction_service.dto;

import com.example.transaction_service.enumeration.TransactionStatus;
import com.example.transaction_service.enumeration.TypeTransaction;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long transactionId;
    private double amount;
    private String currency;
    private String country;
    private Date transactionDate;

    @Enumerated(EnumType.STRING)
    private TypeTransaction typeTransaction;

    @Enumerated (EnumType.STRING)
    private TransactionStatus transactionStatus;
}
