package com.example.transaction_service.service;

import com.example.transaction_service.dto.BankCardDto;
import com.example.transaction_service.entity.BankCard;

import java.util.List;

public interface IBankCardService {
    List<BankCardDto> getAllCard();
    BankCardDto getCardById (Long id);
    BankCardDto createCard(BankCard bankCard);
    BankCardDto updateCard(BankCardDto bankCardDto , Long id);
    void deleteCard(Long id);
}
