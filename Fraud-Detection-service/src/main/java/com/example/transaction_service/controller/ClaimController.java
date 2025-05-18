package com.example.transaction_service.controller;


import com.example.transaction_service.dto.ClaimDto;
import com.example.transaction_service.dto.ClaimResponseDto;
import com.example.transaction_service.entity.Claim;
import com.example.transaction_service.service.IClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final IClaimService claimService;


    @PostMapping
    public ResponseEntity<ClaimDto> createClaim(@RequestBody Claim claim) {
        ClaimDto createdClaim = claimService.createClaim(claim);
        return ResponseEntity.ok(createdClaim);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ClaimDto>> getClaimsByUser(@PathVariable Long userId) {
        List<ClaimDto> claims = claimService.getClaimByUser(userId);
        return ResponseEntity.ok(claims);
    }


    @GetMapping("/pending")
    public ResponseEntity<List<ClaimDto>> getPendingClaims() {
        List<ClaimDto> pendingClaims = claimService.getPendingClaims();
        return ResponseEntity.ok(pendingClaims);
    }


    @PutMapping("/respond/{claimId}")
    public ResponseEntity<ClaimDto> respondToClaim(@PathVariable Long claimId,
                                                   @RequestBody ClaimResponseDto responseDto) {
        ClaimDto updatedClaim = claimService.respondToClaim(claimId, responseDto);
        return ResponseEntity.ok(updatedClaim);
    }
}
