package com.example.transaction_service.controller;

import com.example.transaction_service.dto.TransactionDto;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.repository.ITransactionRepository;
import com.example.transaction_service.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private ITransactionRepository transactionRepository;


    @GetMapping()
    public List<TransactionDto> getAllTransaction ()
    {

        return transactionService.getAllTransaction();
    }

    @GetMapping ("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id)
    {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PostMapping("")
    public TransactionDto createTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.createTransaction(transactionDto);
    }

    @GetMapping("/my-transactions")
    public ResponseEntity<List<TransactionDto>> getMyTransactions(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject(); // ou jwt.getClaim("sub")
        List<TransactionDto> transactions = transactionService.getTransactionsForCurrentUser(keycloakId);
        return ResponseEntity.ok(transactions);
    }


    @PutMapping("/{id}")
    public TransactionDto updateTransaction (@RequestBody TransactionDto transactionDto ,@PathVariable Long id)
    {
       return transactionService.updateTransaction(transactionDto,id);
    }
    @GetMapping("/count")
    public long countTransaction() {
        return transactionRepository.count();
    }

}
