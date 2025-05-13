package com.example.transaction_service.mapper;

import com.example.transaction_service.dto.TransactionDto;
import com.example.transaction_service.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})

public interface ITransactionMapper {
    @Mapping(source = "bankAccount.bankAccountId", target = "bankAccountId")
    TransactionDto toDto (Transaction transaction);
    List<TransactionDto> toDto (List<Transaction> transactions);
    Transaction toEntity (TransactionDto transactionDto);
    List<Transaction>  toEntity (List<Transaction> transactions);
}
