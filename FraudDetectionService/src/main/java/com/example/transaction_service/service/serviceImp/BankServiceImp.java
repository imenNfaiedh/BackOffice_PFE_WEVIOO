package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.BankDto;
import com.example.transaction_service.entitie.Bank;
import com.example.transaction_service.mapper.IBankMapper;
import com.example.transaction_service.repository.IBankRepository;
import com.example.transaction_service.service.IBankService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankServiceImp implements IBankService {

    @Autowired
    private IBankRepository bankRepository;
    @Autowired
    private IBankMapper bankMapper;



    @Override
    public List<BankDto> getAllBank() {
        List<Bank> banks = bankRepository.findAll();
        return bankMapper.toDto(banks);

    }

    @Override
    public BankDto getByIdBank(Long id) {
        Optional<Bank> bank = bankRepository.findById(id);
        if (bank.isPresent()) {
            return bankMapper.toDto(bank.get());
        } else {
            System.out.println("bank not found");
            return null;
        }
    }

    @Override
    public BankDto createBank(Bank bank) {
        bank = bankRepository.save(bank);
        return bankMapper.toDto(bank);
    }

    @Override
    public BankDto updateBank(BankDto bankDto, Long bankId) {
        Optional<Bank> bankOptional = bankRepository.findById(bankId);
        if (!bankOptional.isPresent()) {
            throw new RuntimeException("not found");
        }

        Bank bank = bankOptional.get();
        bank.setBankName(bankDto.getBankName());


        return bankMapper.toDto(bankRepository.save(bank));

    }

    @Override
    public void deleteBank(Long id) {

    }
}
