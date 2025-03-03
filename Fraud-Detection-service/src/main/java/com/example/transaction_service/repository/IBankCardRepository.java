package com.example.transaction_service.repository;

import com.example.transaction_service.entity.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBankCardRepository extends JpaRepository<BankCard,Long> {
}
