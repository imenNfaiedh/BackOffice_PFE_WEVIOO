package com.example.transaction_service.service;

import com.example.transaction_service.dto.BankDto;
import com.example.transaction_service.entitie.Bank;

import java.util.List;

public interface IBankService {

    List<BankDto> getAllBank();
    BankDto getByIdBank (Long idBank);
    BankDto crateBank(Bank bank);
    BankDto updateBank(BankDto bankDto , Long idBank);
    void deleteBank (Long id);


}
