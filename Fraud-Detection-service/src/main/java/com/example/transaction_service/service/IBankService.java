package com.example.transaction_service.service;

import com.example.transaction_service.dto.BankDto;
import com.example.transaction_service.entity.Bank;

import java.util.List;

public interface IBankService {

    List<BankDto> getAllBank();
    BankDto getByIdBank (Long id);
    BankDto createBank(Bank bank);
    BankDto updateBank(BankDto bankDto , Long bankId);
    void deleteBank (Long id);


}
