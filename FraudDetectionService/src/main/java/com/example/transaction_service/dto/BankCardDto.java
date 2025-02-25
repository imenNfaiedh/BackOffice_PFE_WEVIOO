package com.example.transaction_service.dto;

import com.example.transaction_service.enumeration.BankCardStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankCardDto {

    private Long bankCardId;
    private Long cardNumber;
    private Date expirationDate;
    private int pinCode;
    private int cvv;
    private String cardHolderName;

    @Enumerated(EnumType.STRING)
    private BankCardStatus bankCardStatus;
}
