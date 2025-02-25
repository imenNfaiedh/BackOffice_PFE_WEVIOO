package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.BankAccountDto;
import com.example.transaction_service.entitie.BankAccount;
import com.example.transaction_service.mapper.IBankAccountMapper;
import com.example.transaction_service.repository.IBankAccountRepository;
import com.example.transaction_service.service.IBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BankAccountServiceImp implements IBankAccountService {

    @Autowired
    private IBankAccountRepository bankAccountRepository;
    @Autowired
    private IBankAccountMapper bankAccountMapper;
    @Override
    public List<BankAccountDto> getAllAccount() {
         List<BankAccount> bankAccounts= bankAccountRepository.findAll();
        return bankAccountMapper.toDto(bankAccounts);
    }

    @Override
    public BankAccountDto getAccountById(Long id) {
       Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
       if (bankAccount.isPresent())
       {
           return bankAccountMapper.toDto(bankAccount.get());
       }
       else {
           System.out.println("Account not found");
           return null;
       }
    }

    @Override
    public BankAccountDto createAccount(BankAccount bankAccount) {
        bankAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDto(bankAccount);
    }

    @Override
    public BankAccountDto updateAccount(BankAccountDto bankAccountDto, Long id) {
        Optional<BankAccount> accountOptional = bankAccountRepository.findById(id);
        if(!accountOptional.isPresent())
        {
            throw new RuntimeException("not found");
        }
        BankAccount bankAccount = accountOptional.get();
        bankAccount.setAccountNumber(bankAccountDto.getAccountNumber());
        bankAccount.setTypeBankAccount(bankAccountDto.getTypeBankAccount());
        bankAccount.setBalance(bankAccountDto.getBalance());
        bankAccount.setOpeningDate(bankAccountDto.getOpeningDate());

        return bankAccountMapper.toDto(bankAccountRepository.save(bankAccount));



    }

    @Override
    public void deleteAccount(Long id) {

    }
}
