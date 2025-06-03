package com.example.transaction_service.dto;

import com.example.transaction_service.entity.User;
import com.example.transaction_service.enumeration.TypeBankAccount;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BankAccountDto {

    private Long bankAccountId;
    private Long accountNumber;
    private Date openingDate;
    private BigDecimal balance;
    private Integer fraudCount = 0;
    private Boolean isBlocked = false;
    @Enumerated(EnumType.STRING)
    private TypeBankAccount typeBankAccount;

    private String userFirstName;
    private String userLastName;


}
