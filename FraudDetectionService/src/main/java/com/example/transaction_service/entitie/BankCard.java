package com.example.transaction_service.entitie;

import com.example.transaction_service.enumeration.BankCardStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@AllArgsConstructor
@Entity
@NoArgsConstructor

public class BankCard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long bankCardId;
    private Long cardNumber;
    private Date expirationDate;
    private int pinCode;
    private int cvv;
    private String cardHolderName;

    @Enumerated(EnumType.STRING)
    private BankCardStatus bankCardStatus;

    @ManyToOne
    @JoinColumn(name = "bank_Account_id")
    private BankAccount bankAccount;




}
