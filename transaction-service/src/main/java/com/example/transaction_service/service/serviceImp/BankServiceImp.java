package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.BankDto;
import com.example.transaction_service.entitie.Bank;
import com.example.transaction_service.mapper.IBankMapper;
import com.example.transaction_service.repository.IBankRepository;
import com.example.transaction_service.service.IBankService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Data
@Service
public class BankServiceImp implements IBankService {

    @Autowired
    IBankRepository bankRepository;
    @Autowired
    IBankMapper bankMapper;

    public BankServiceImp(IBankRepository bankRepository, IBankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    @Override
    public List<BankDto> getAllBank() {
        List<Bank> banks = bankRepository.findAll();
        return bankMapper.toDto(banks);

    }

    @Override
    public BankDto getByIdBank(Long idBank) {
        Optional<Bank> bank = bankRepository.findById(idBank);
        if (bank.isPresent()) {
            return bankMapper.toDto(bank.get());
        } else {
            System.out.println("bank not found");
            return null;
        }
    }

    @Override
    public BankDto crateBank(Bank bank) {
        bank = bankRepository.save(bank);
        return bankMapper.toDto(bank);
    }

    @Override
    public BankDto updateBank(BankDto bankDto, Long idBank) {
        Optional<Bank> bankOptional = bankRepository.findById(idBank);
        if (!bankOptional.isPresent()) {
            throw new RuntimeException("not found");
        }

        Bank bank = bankOptional.get();
        bank.setNameBank(bankDto.getNameBank());


        return bankMapper.toDto(bankRepository.save(bank));

    }

    @Override
    public void deleteBank(Long id) {

    }
}
