package com.example.transaction_service.mapper;

import com.example.transaction_service.dto.BankDto;
import com.example.transaction_service.entitie.Bank;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface IBankMapper {

    BankDto toDto (Bank bank);
    Bank toEntity (BankDto bankDto);
    List<BankDto> toDto (List<Bank> banks);
    List<Bank> toEntity (List<BankDto> bankDtoList);
}
