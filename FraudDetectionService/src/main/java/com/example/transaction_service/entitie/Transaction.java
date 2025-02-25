package com.example.transaction_service.entitie;

import com.example.transaction_service.enumeration.TransactionStatus;
import com.example.transaction_service.enumeration.TypeTransaction;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable  {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private Long transactionId;
    private double amount;
    private String currency;
    private String country;
    private Date transactionDate;

    @Enumerated (EnumType.STRING)
    private TypeTransaction typeTransaction;

    @Enumerated (EnumType.STRING)
    private TransactionStatus transactionStatus;

    @ManyToOne
    @JoinColumn(name = "bank_Account_id")
    private BankAccount bankAccount;


}
