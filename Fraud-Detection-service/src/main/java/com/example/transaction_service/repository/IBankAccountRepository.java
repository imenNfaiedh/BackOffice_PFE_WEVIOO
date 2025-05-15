package com.example.transaction_service.repository;

import com.example.transaction_service.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface IBankAccountRepository extends JpaRepository<BankAccount,Long> {
    List<BankAccount> findByUser_UserId(Long userId);
}
