package com.example.transaction_service.repository;

import com.example.transaction_service.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

 
public interface IBankAccountRepository extends JpaRepository<BankAccount,Long> {
}
