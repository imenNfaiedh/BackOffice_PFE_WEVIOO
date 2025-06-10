package com.example.transaction_service.repository;

import com.example.transaction_service.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IClaimRepository extends JpaRepository<Claim,Long> {
    List<Claim> findByStatus(String status);
    List<Claim> findByUser_UserId(Long userId);

    List<Claim> findByUser_keycloakId(String user_keycloakId );

    long countByStatus(String status);
}
