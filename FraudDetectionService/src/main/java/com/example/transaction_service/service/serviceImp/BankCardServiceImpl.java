package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.BankCardDto;

import com.example.transaction_service.entitie.BankCard;
import com.example.transaction_service.mapper.IBankCardMapper;
import com.example.transaction_service.repository.IBankCardRepository;
import com.example.transaction_service.service.IBankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankCardServiceImpl implements IBankCardService {

    @Autowired
    private IBankCardRepository bankCardRepository;
    @Autowired
    private IBankCardMapper bankCardMapper;
    @Override
    public List<BankCardDto> getAllCard() {
        List<BankCard> bankCards =bankCardRepository.findAll();
        return bankCardMapper.toDto(bankCards);
    }

    @Override
    public BankCardDto getCardById(Long id) {
        Optional<BankCard> bankCard =bankCardRepository.findById(id);
        if (bankCard.isPresent())
        {
            return bankCardMapper.toDto(bankCard.get());

        }
        else
            System.out.println("card not found");
        return null;

    }

    @Override
    public BankCardDto createCard(BankCard bankCard) {
        bankCard =bankCardRepository.save(bankCard);
        return bankCardMapper.toDto(bankCard);
    }

    @Override
    public BankCardDto updateCard(BankCardDto bankCardDto, Long id) {
        Optional<BankCard> bankCardOptional = bankCardRepository.findById(id);
        if (!bankCardOptional.isPresent())
        {
            throw new RuntimeException("card not found");

        }
        BankCard bankCard =bankCardOptional.get();
        bankCard.setCardNumber(bankCardDto.getCardNumber());
        bankCard.setExpirationDate(bankCardDto.getExpirationDate());
        bankCard.setCvv(bankCardDto.getCvv());
        bankCard.setPinCode(bankCardDto.getPinCode());
        bankCard.setCardHolderName(bankCardDto.getCardHolderName());


        return bankCardMapper.toDto(bankCardRepository.save(bankCard));
    }

    @Override
    public void deleteCard(Long id) {

    }
}
