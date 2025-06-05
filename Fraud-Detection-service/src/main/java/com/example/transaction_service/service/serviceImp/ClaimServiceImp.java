package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.ClaimDto;
import com.example.transaction_service.dto.ClaimResponseDto;
import com.example.transaction_service.entity.Claim;
import com.example.transaction_service.entity.User;
import com.example.transaction_service.exception.NotFoundException;
import com.example.transaction_service.mapper.IClaimMapper;
import com.example.transaction_service.repository.IClaimRepository;
import com.example.transaction_service.repository.IUserRepository;
import com.example.transaction_service.service.IClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClaimServiceImp implements IClaimService {

    @Autowired
    private IClaimRepository claimRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IClaimMapper claimMapper;
    @Override
    public ClaimDto createClaimForConnectedUser(Claim claim, String keycloakId) {
        claim.setDateReclamation(LocalDateTime.now());
        claim.setStatus("EN_ATTENTE");
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(()->   new NotFoundException("User with not found with id" + claim.getUser().getUserId()));
        claim.setUser(user);
        Claim claimSaved = claimRepository.save(claim);
        return claimMapper.toDto(claimSaved);
    }



    @Override
    public List<ClaimDto> getClaimByUser(Long userId) {
        return claimMapper.toDto(claimRepository.findByUser_UserId(userId));
    }

    @Override
    public List<ClaimDto> getPendingClaims() {

        return claimMapper.toDto(claimRepository.findByStatus("EN_ATTENTE"));
    }

    @Override
    public ClaimDto respondToClaim(Long claimId, ClaimResponseDto responseDto) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new NotFoundException("Réclamation non trouvée"));

        claim.setResponseAdmin(responseDto.getResponseAdmin());
        claim.setStatus("TRAITEE");

        return claimMapper.toDto(claimRepository.save(claim));
        }

    @Override
    public List<ClaimDto> getClaimForCurrentUser(String keycloakId) {
        List<Claim> claims = claimRepository.findByUser_keycloakId(keycloakId);
        return claimMapper.toDto(claims);
    }

    @Override
    public ClaimDto getClaimById(Long id) {
        Optional<Claim> claim =claimRepository.findById(id);
        if (claim.isPresent()){
            return claimMapper.toDto(claim.get());
        }
        else {
            throw new NotFoundException("reclamation notfound with id : " +id);
        }

    }
}
