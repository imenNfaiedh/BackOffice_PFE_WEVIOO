package com.example.transaction_service.repository;

import com.example.transaction_service.entity.BankAccount;
import com.example.transaction_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBankAccountRepository extends JpaRepository<BankAccount,Long> {
    List<BankAccount> findByUser_UserId(Long userId);

    List<BankAccount> findByUser_KeycloakId(String user_keycloakId);

   // Optional<BankAccount> findByIdAndUser_UserId(Long accountId, Long userId);
}
