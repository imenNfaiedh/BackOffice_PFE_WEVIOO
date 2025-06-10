package com.example.transaction_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClaimStatsDto {
    private long pendingCount;
    private long treatedCount;
}
