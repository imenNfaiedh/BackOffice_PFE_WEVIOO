package com.example.transaction_service.controller;

import com.example.transaction_service.dto.ClaimStatsDto;
import com.example.transaction_service.repository.IBankAccountRepository;
import com.example.transaction_service.repository.IBankRepository;
import com.example.transaction_service.repository.ITransactionRepository;
import com.example.transaction_service.repository.IUserRepository;
import com.example.transaction_service.service.IClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private IUserRepository userRepo;
    @Autowired
    private IBankRepository bankRepo;
    @Autowired
    private IBankAccountRepository accountRepo;
    @Autowired
    private ITransactionRepository transactionRepo;
    @Autowired
    private IClaimService claimService;


    @GetMapping("/stats")
    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("users", userRepo.count());
        stats.put("banks", bankRepo.count());
        stats.put("accounts", accountRepo.count());
        stats.put("transactions", transactionRepo.count());

        return stats;
    }

    @GetMapping("/claimStatus")
    public ResponseEntity<ClaimStatsDto> getClaimStats() {
        return ResponseEntity.ok(claimService.getClaimStats());
    }
}

