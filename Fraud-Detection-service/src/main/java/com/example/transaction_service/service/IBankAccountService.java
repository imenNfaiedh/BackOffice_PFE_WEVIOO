package com.example.transaction_service.service;

import com.example.transaction_service.dto.BankAccountDto;
import com.example.transaction_service.entity.BankAccount;

import java.util.List;

public interface IBankAccountService {
    public List<BankAccountDto> getBankAccountFoCurrentUser(String KeycloakId);
    List<BankAccountDto> getAllAccount();

    BankAccountDto getAccountById(Long id);
    public BankAccountDto createAccount(BankAccountDto dto)    ;

    BankAccountDto updateAccount (BankAccountDto bankAccountDto, Long id);
    void deleteAccount (Long id);

}
