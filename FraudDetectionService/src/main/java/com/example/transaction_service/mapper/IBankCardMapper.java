package com.example.transaction_service.mapper;

import com.example.transaction_service.dto.BankCardDto;
import com.example.transaction_service.entitie.BankCard;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring", uses = {})

public interface IBankCardMapper {

        BankCardDto toDto (BankCard bankCard);

        List<BankCardDto> toDto (List<BankCard> bankCards);

        BankCard toEntity (BankCardDto bankCardDto);
        List<BankCard> toEntity (List<BankCardDto> bankCardDtos);



}
