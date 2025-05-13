package com.example.transaction_service.controller;

import com.example.transaction_service.dto.BankAccountDto;

import com.example.transaction_service.entity.BankAccount;
import com.example.transaction_service.repository.IBankAccountRepository;
import com.example.transaction_service.service.IBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {
    @Autowired
    IBankAccountService bankAccountService;
    @Autowired
    IBankAccountRepository bankAccountRepository;

    @GetMapping()
    public List<BankAccountDto> getAllAccount() {
        return bankAccountService.getAllAccount();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDto> getAccountById(@PathVariable Long id)
    {
        return ResponseEntity.ok(bankAccountService.getAccountById(id));
    }

    @PostMapping("")
    public BankAccountDto createAccount (@RequestBody BankAccount bankAccount)
    {
        return bankAccountService.createAccount(bankAccount);
    }

    @PutMapping("{id}")

    public BankAccountDto upadateAccount (@PathVariable Long id, @RequestBody BankAccountDto bankAccountDto )

    {
        bankAccountDto.setBankAccountId(id);
        return bankAccountService.updateAccount(bankAccountDto,id);
    }

    @GetMapping("/count")
    public long countAccount() {
        return bankAccountRepository.count();
    }

}
