package com.example.transaction_service.controller;

import com.example.transaction_service.dto.BankDto;
import com.example.transaction_service.service.IBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping ("/bank")
public class BankController {

    @Autowired
    private IBankService bankService;

    @GetMapping()
    public List<BankDto> getAllBank()
    {
        return bankService.getAllBank();
    }

}
