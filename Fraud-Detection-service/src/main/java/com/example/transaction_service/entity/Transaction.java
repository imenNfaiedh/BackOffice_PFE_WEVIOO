package com.example.transaction_service.entity;

import com.example.transaction_service.enumeration.TransactionStatus;
import com.example.transaction_service.enumeration.TypeTransaction;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
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
    @Column(name = "FDS004_TRANSACTION_ID")
    private Long transactionId;
    @Column(name = "FDS004_AMOUNT")
    private BigDecimal amount;
    @Column(name = "FDS004_CURRENCY")
    private String currency;
    @Column(name = "FDS004_COUNTRY")
    private String country;
    @Column(name = "FDS004_TRANSACTION_DATE")
    private Date transactionDate;

    @Column(name = "FDS004_IS_SEND_NOTIF")
    private Boolean isSendNotification;

    @Enumerated (EnumType.STRING)
    private TypeTransaction typeTransaction;

    @Enumerated (EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Getter
    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;




}
