package com.example.transaction_service.entity;

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
@Table(name = "FDS003T_BANK_CARD")
public class BankCard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "FDS003_BANK_CARD_ID")
    private Long bankCardId;
    @Column(name = "FDS003_BANK_NUMBER")
    private Long cardNumber;
    @Column(name = "FDS003_EXPIRATION_DATE")
    private Date expirationDate;
    @Column(name = "FDS003_PIN_CODE")
    private int pinCode;
    @Column(name = "FDS003_CODE_VV")
    private int cvv;
    @Column(name = "FDS003_CARD_HOLDER_NAME")
    private String cardHolderName;

    @Enumerated(EnumType.STRING)
    private BankCardStatus bankCardStatus;

    @ManyToOne
    @JoinColumn(name = "bank_Account_id")
    private BankAccount bankAccount;




}
