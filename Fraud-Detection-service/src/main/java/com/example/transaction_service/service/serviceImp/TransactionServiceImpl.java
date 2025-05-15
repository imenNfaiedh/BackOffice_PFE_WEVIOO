package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.TransactionDto;
import com.example.transaction_service.entity.BankAccount;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.enumeration.TransactionStatus;
import com.example.transaction_service.exception.NotFoundException;
import com.example.transaction_service.mapper.ITransactionMapper;
import com.example.transaction_service.repository.IBankAccountRepository;
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
    @Autowired
    IBankAccountRepository bankAccountRepository;

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
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        Transaction transaction = transactionMapper.toEntity(transactionDto);
        BankAccount bankAccount = bankAccountRepository.findById(transactionDto.getBankAccountId())
                .orElseThrow(() -> new NotFoundException("Bank account not found with ID " + transactionDto.getBankAccountId()));
        transaction.setBankAccount(bankAccount);

        transaction.setTransactionStatus(TransactionStatus.VALID);
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

    public TransactionDto updateTransactionStatus(Long transactionId, boolean isFraudulent) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction non trouvée avec l'ID " + transactionId));

        if (isFraudulent) {

            transaction.setTransactionStatus(TransactionStatus.SUSPICIOUS);
        } else {
            transaction.setTransactionStatus(TransactionStatus.VALID);
        }

        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }
}
