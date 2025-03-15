package com.example.transaction_service.controller;

import com.example.transaction_service.dto.TransactionEnrichedDto;
import com.example.transaction_service.service.serviceImp.TransactionServiceEnriched;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionEnrichedController {

    @Autowired
    private TransactionServiceEnriched transactionServiceEnriched;


    @GetMapping("/enrich/{transactionId}")
    public ResponseEntity<TransactionEnrichedDto> enrichTransaction(@PathVariable Long transactionId) {
        TransactionEnrichedDto enrichedDto = transactionServiceEnriched.enrichTransaction(transactionId);
        return ResponseEntity.ok(enrichedDto);
    }
}

