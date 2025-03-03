package com.example.transaction_service.controller;

import com.example.transaction_service.dto.BankCardDto;
import com.example.transaction_service.entity.BankCard;
import com.example.transaction_service.service.IBankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class BankCardController {
    @Autowired
    private IBankCardService bankCardService;

    @GetMapping()
    public List<BankCardDto> getAllBankCard()
    {
        return bankCardService.getAllCard();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankCardDto> getByIdBankCard (@PathVariable Long id)
    {
        return ResponseEntity.ok(bankCardService.getCardById(id));
    }

    @PostMapping("")
    public BankCardDto createBankCard (@RequestBody BankCard bankCard)
    {
        return bankCardService.createCard(bankCard);
    }

    @PutMapping("/{id}")
    public  BankCardDto updateBankCard(@PathVariable Long id, @RequestBody BankCardDto bankCardDto)
    {
        bankCardDto.setBankCardId(id);
        return bankCardService.updateCard(bankCardDto,id);
    }
}
