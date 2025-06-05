package com.example.transaction_service.controller;


import com.example.transaction_service.dto.ClaimDto;
import com.example.transaction_service.dto.ClaimResponseDto;
import com.example.transaction_service.entity.Claim;
import com.example.transaction_service.service.IClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final IClaimService claimService;


    @PostMapping
    public ResponseEntity<ClaimDto> createClaim(@RequestBody Claim claim, @AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        ClaimDto createdClaim = claimService.createClaimForConnectedUser(claim,keycloakId);
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

    @GetMapping("myClaim")
    public ResponseEntity<List<ClaimDto>> getMyClaim(@AuthenticationPrincipal Jwt jwt)
    {
        String keycloakId = jwt.getSubject();
        List<ClaimDto> claimDtos =claimService.getClaimForCurrentUser(keycloakId);
        return ResponseEntity.ok(claimDtos);
    }
    @GetMapping("{id}")
    public ResponseEntity<ClaimDto> getClaimById(@PathVariable Long id)
    {
        ClaimDto claimDto = claimService.getClaimById(id);
        return ResponseEntity.ok(claimDto);
    }


}
