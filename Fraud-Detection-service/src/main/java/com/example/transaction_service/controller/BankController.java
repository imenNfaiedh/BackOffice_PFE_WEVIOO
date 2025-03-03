package com.example.transaction_service.controller;

import com.example.transaction_service.dto.BankDto;
import com.example.transaction_service.entity.Bank;
import com.example.transaction_service.service.IBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/banks")
public class BankController {

    @Autowired
    private IBankService bankService;

    @GetMapping()
    public List<BankDto> getAllBank()
    {
        return bankService.getAllBank();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankDto> getByIdBank (@PathVariable Long id)
    {
        return ResponseEntity.ok(bankService.getByIdBank(id));
    }

    @PostMapping("")
    public BankDto createBank (@RequestBody Bank bank)
    {
        return bankService.createBank(bank);
    }

    @PutMapping("/{id}")
    public  BankDto updateBank(@PathVariable Long id, @RequestBody BankDto bankDto)
    {
        bankDto.setBankId(id);
        return bankService.updateBank(bankDto,id);
    }

}
