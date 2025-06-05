package com.example.transaction_service.service;

import com.example.transaction_service.dto.ClaimDto;
import com.example.transaction_service.dto.ClaimResponseDto;
import com.example.transaction_service.entity.Claim;

import java.util.List;

public interface IClaimService {


    ClaimDto createClaimForConnectedUser(Claim claim, String keycloakId);

    List<ClaimDto> getClaimByUser (Long userId);
    List<ClaimDto> getPendingClaims();
    ClaimDto respondToClaim(Long claimId, ClaimResponseDto responseDto);
    List<ClaimDto> getClaimForCurrentUser(String keycloakId);

    ClaimDto getClaimById(Long id);

    void deleteClaim(Long id);

}
