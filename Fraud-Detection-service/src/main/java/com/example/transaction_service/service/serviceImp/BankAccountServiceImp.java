package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.BankAccountDto;
import com.example.transaction_service.entity.BankAccount;
import com.example.transaction_service.exception.NotFoundException;
import com.example.transaction_service.mapper.IBankAccountMapper;
import com.example.transaction_service.repository.IBankAccountRepository;
import com.example.transaction_service.service.IBankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@Slf4j
public class BankAccountServiceImp implements IBankAccountService {

    @Autowired
    private IBankAccountRepository bankAccountRepository;
    @Autowired
    private IBankAccountMapper bankAccountMapper;



    public List<BankAccountDto> getBankAccountFoCurrentUser(String KeycloakId)
    {
        List<BankAccount> bankAccounts =bankAccountRepository.findByUser_KeycloakId(KeycloakId);
        return bankAccountMapper.toDto(bankAccounts);
    }
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

           throw new NotFoundException("Account with ID " + id + " not found");
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
            throw new NotFoundException("Account with ID " + id + " Not Found");
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

            if (!bankAccountRepository.existsById(id)) {
                throw new NotFoundException("Claim not found with ID: " + id);
            }
            bankAccountRepository.deleteById(id);
        }

    }

