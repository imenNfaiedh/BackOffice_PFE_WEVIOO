package com.example.transaction_service.service;

import com.example.transaction_service.dto.ClaimDto;
import com.example.transaction_service.dto.ClaimResponseDto;
import com.example.transaction_service.entity.Claim;

import java.util.List;

public interface IClaimService {
    ClaimDto createClaim (Claim claim);
    List<ClaimDto> getClaimByUser (Long userId);
    List<ClaimDto> getPendingClaims();
    ClaimDto respondToClaim(Long claimId, ClaimResponseDto responseDto);
}
