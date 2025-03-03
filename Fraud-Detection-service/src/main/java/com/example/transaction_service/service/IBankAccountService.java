package com.example.transaction_service.service;

import com.example.transaction_service.dto.BankAccountDto;
import com.example.transaction_service.entity.BankAccount;

import java.util.List;

public interface IBankAccountService {
    List<BankAccountDto> getAllAccount();

    BankAccountDto getAccountById(Long id);
    BankAccountDto createAccount(BankAccount bankAccount);
    BankAccountDto updateAccount (BankAccountDto bankAccountDto, Long id);
    void deleteAccount (Long id);

}
