package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.TransactionDto;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.exception.NotFoundException;
import com.example.transaction_service.mapper.ITransactionMapper;
import com.example.transaction_service.repository.ITransactionRepository;
import com.example.transaction_service.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl  implements ITransactionService {
    @Autowired
    ITransactionRepository transactionRepository;
    @Autowired
    ITransactionMapper transactionMapper;

    @Override
    public List<TransactionDto> getAllTransaction() {
        List<Transaction> transactions =transactionRepository.findAll();
        return transactionMapper.toDto(transactions);
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        Optional<Transaction> transaction =transactionRepository.findById(id);
        if (transaction.isPresent())
        {
           return transactionMapper.toDto(transaction.get());
        }
        else {
            throw new NotFoundException("Transaction with ID " + id + " not found");
        }
    }

    @Override
    public TransactionDto createTransaction(Transaction transaction) {
         transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionDto updateTransaction(TransactionDto transactionDto, Long id) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        if (!optionalTransaction.isPresent()) {
            throw new NotFoundException("Transaction with ID " + id + " not found");
        }

        Transaction transaction = optionalTransaction.get();
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setTransactionStatus(transactionDto.getTransactionStatus());
        transaction.setTypeTransaction(transactionDto.getTypeTransaction());
        transaction.setCountry(transactionDto.getCountry());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setCurrency(transactionDto.getCurrency());

        return transactionMapper.toDto(transactionRepository.save(transaction));


    }

    @Override
    public void deleteTransaction(Long id) {

    }
}
