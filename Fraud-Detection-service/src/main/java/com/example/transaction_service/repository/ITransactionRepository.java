package com.example.transaction_service.repository;

import com.example.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByBankAccount_User_UserIdAndTransactionDateAfter(Long userId, Date afterDate);
}
