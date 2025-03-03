package com.example.transaction_service.dto;

import com.example.transaction_service.enumeration.TypeBankAccount;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BankAccountDto {

    private Long bankAccountId;
    private Long accountNumber;
    private Date openingDate;
    private double balance;
    @Enumerated(EnumType.STRING)
    private TypeBankAccount typeBankAccount;
}
