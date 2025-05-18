package com.example.transaction_service.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ClaimDto {
    private Long id;
    private String subject;
    private String message;
    private LocalDateTime dateReclamation;

    private String status;
    private String responseAdmin;

    private Long userId;
}
