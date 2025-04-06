package com.example.kafka_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FraudAlertDto {
    private Long userId;
    private double amount;
    private long timestamp;
    private boolean suspiciousActivity;
}
