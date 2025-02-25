package com.example.transaction_service.mapper;

import com.example.transaction_service.dto.BankAccountDto;
import com.example.transaction_service.entitie.BankAccount;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})


public interface IBankAccountMapper {
    BankAccountDto toDto (BankAccount bankAccount);
    List<BankAccountDto> toDto(List<BankAccount> bankAccounts);
   List<BankAccount>  toEntity (List<BankAccountDto> bankAccountDtoList);
    BankAccount toEntity(BankAccountDto bankAccountDto);


}
