package com.example.transaction_service.repository;

import com.example.transaction_service.entitie.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBankRepository extends JpaRepository<Bank,Long> {
}
