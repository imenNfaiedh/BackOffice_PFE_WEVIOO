package com.example.transaction_service.entity;

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
@Table(name = "FDS004T_TRANSACTION")
public class Transaction implements Serializable  {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "FDS003_TRANSACTION_ID")
    private Long transactionId;
    @Column(name = "FDS003_AMOUNT")
    private double amount;
    @Column(name = "FDS003_CURRENCY")
    private String currency;
    @Column(name = "FDS003_COUNTRY")
    private String country;
    @Column(name = "FDS003_TRANSACTION_DATE")
    private Date transactionDate;

    @Enumerated (EnumType.STRING)
    private TypeTransaction typeTransaction;

    @Enumerated (EnumType.STRING)
    private TransactionStatus transactionStatus;

    @ManyToOne
    @JoinColumn(name = "bank_Account_id")
    private BankAccount bankAccount;


}
