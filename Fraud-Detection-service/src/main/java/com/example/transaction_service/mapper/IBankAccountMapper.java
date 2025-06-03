package com.example.transaction_service.mapper;

import com.example.transaction_service.dto.BankAccountDto;
import com.example.transaction_service.entity.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})


public interface IBankAccountMapper {
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.lastName", target = "userLastName")
    BankAccountDto toDto (BankAccount bankAccount);
    List<BankAccountDto> toDto(List<BankAccount> bankAccounts);
   List<BankAccount>  toEntity (List<BankAccountDto> bankAccountDtoList);
    BankAccount toEntity(BankAccountDto bankAccountDto);


}
