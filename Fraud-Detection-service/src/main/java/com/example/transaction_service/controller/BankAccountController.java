package com.example.transaction_service.controller;

import com.example.transaction_service.dto.BankAccountDto;

import com.example.transaction_service.entity.BankAccount;
import com.example.transaction_service.repository.IBankAccountRepository;
import com.example.transaction_service.service.IBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {
    @Autowired
    IBankAccountService bankAccountService;
    @Autowired
    IBankAccountRepository bankAccountRepository;

    @GetMapping("myBankAccounts")
    public ResponseEntity<List<BankAccountDto>> getMyBankAccount(@AuthenticationPrincipal Jwt jwt)
    {
        String keycloakId =jwt.getSubject();
        List<BankAccountDto> bankAccountDtos =bankAccountService.getBankAccountFoCurrentUser(keycloakId);
        return ResponseEntity.ok(bankAccountDtos);
    }

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
    @PutMapping("/toggle-block/{id}")
    public ResponseEntity<Map<String, Object>> toggleBlockStatus(@PathVariable Long id) {
        Optional<BankAccount> optional = bankAccountRepository.findById(id);
        if (optional.isPresent()) {
            BankAccount account = optional.get();
            boolean wasBlocked = account.getIsBlocked();
            account.setIsBlocked(!wasBlocked);  // Inverse l'état actuel
            if (wasBlocked) {
                // Si le compte était bloqué et on le débloque, on remet fraudCount à 0
                account.setFraudCount(0);
            }
            bankAccountRepository.save(account);
            Map<String, Object> response = new HashMap<>();
            response.put("message", account.getIsBlocked() ? "Compte bloqué." : "Compte débloqué et compteur de fraudes réinitialisé.");
            response.put("isBlocked", account.getIsBlocked());
            response.put("fraudCount", account.getFraudCount());

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/count")
    public long countAccount() {
        return bankAccountRepository.count();
    }

}
