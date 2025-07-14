package com.example.transaction_service.service;

import com.example.transaction_service.dto.TransactionDto;
import com.example.transaction_service.entity.Transaction;

import java.util.List;

public interface ITransactionService {
    List<TransactionDto> getAllTransaction();

    TransactionDto getTransactionById (Long id);
    TransactionDto createTransaction(TransactionDto transactionDto);
    TransactionDto updateTransaction (TransactionDto transactionDto , Long id);
    void deleteTransaction (Long id);

    List<TransactionDto> getTransactionsForCurrentUser(String keycloakId);

}
