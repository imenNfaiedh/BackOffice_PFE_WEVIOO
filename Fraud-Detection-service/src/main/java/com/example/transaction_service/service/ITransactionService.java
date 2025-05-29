package com.example.transaction_service.service;

import com.example.transaction_service.dto.TransactionDto;
import com.example.transaction_service.entity.Transaction;

import java.util.List;

public interface ITransactionService {
    List<TransactionDto> getAllTransaction();

    TransactionDto getTransactionById (Long id);
    public TransactionDto createTransaction(TransactionDto transactionDto);
    TransactionDto updateTransaction (TransactionDto transactionDto , Long id);
    void deleteTransaction (Long id);

    public List<TransactionDto> getTransactionsForCurrentUser(String keycloakId);
}
