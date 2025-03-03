package com.example.transaction_service.controller;

import com.example.transaction_service.dto.TransactionDto;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;


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
    public TransactionDto createTransaction(@RequestBody Transaction transaction)
    {
        return transactionService.createTransaction(transaction);
    }

    @PutMapping("/{id}")
    public TransactionDto updateTransaction (@RequestBody TransactionDto transactionDto ,@PathVariable Long id)
    {
       return transactionService.updateTransaction(transactionDto,id);
    }
}
