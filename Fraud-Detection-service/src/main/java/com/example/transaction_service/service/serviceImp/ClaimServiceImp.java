package com.example.transaction_service.service.serviceImp;

import com.example.transaction_service.dto.ClaimDto;
import com.example.transaction_service.dto.ClaimResponseDto;
import com.example.transaction_service.dto.ClaimStatsDto;
import com.example.transaction_service.entity.Claim;
import com.example.transaction_service.entity.User;
import com.example.transaction_service.exception.NotFoundException;
import com.example.transaction_service.mapper.IClaimMapper;
import com.example.transaction_service.repository.IClaimRepository;
import com.example.transaction_service.repository.IUserRepository;
import com.example.transaction_service.service.IClaimService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClaimServiceImp implements IClaimService {

    @Autowired
    private IClaimRepository claimRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IClaimMapper claimMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public ClaimDto createClaimForConnectedUser(Claim claim, String keycloakId) {
        claim.setDateReclamation(LocalDateTime.now());
        claim.setStatus("EN_ATTENTE");
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("User not found with keycloakId: " + keycloakId));
        claim.setUser(user);
        Claim claimSaved = claimRepository.save(claim);
        return claimMapper.toDto(claimSaved);
    }

    @Override
    public List<ClaimDto> getPendingClaims() {

        return claimMapper.toDto(claimRepository.findByStatus("EN_ATTENTE"));
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

    @Override
    public void deleteClaim(Long id) {
        if (!claimRepository.existsById(id)) {
            throw new RuntimeException("Claim not found with ID: " + id);
        }
        claimRepository.deleteById(id);
    }

    @Override
    public List<ClaimDto> getClaimByUser(Long userId) {
        return claimMapper.toDto(claimRepository.findByUser_UserId(userId));
    }



    @Override
    public ClaimDto respondToClaim(Long claimId, ClaimResponseDto responseDto) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new NotFoundException("Réclamation non trouvée"));

        claim.setResponseAdmin(responseDto.getResponseAdmin());
        claim.setStatus("TRAITEE");

        // Envoyer notification au service Kafka
        Map<String, Object> message = new HashMap<>();
        message.put("email", claim.getUser().getEmail());
        message.put("firstName", claim.getUser().getFirstName());
        message.put("lastName", claim.getUser().getLastName());

        message.put("subject", "📬 Réponse à votre réclamation");
        message.put("content", responseDto.getResponseAdmin());

        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send("claim-response-topic", jsonMessage);
            log.info("📤 Kafka message sent: {}", jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("❌ JSON serialization error", e);
        }

        return claimMapper.toDto(claimRepository.save(claim));
    }


    @Override
    public ClaimStatsDto getClaimStats() {
        long pendingCount = claimRepository.countByStatus("EN_ATTENTE");
        long treatedCount = claimRepository.countByStatus("TRAITEE");
        return new ClaimStatsDto(pendingCount, treatedCount);
    }




}
