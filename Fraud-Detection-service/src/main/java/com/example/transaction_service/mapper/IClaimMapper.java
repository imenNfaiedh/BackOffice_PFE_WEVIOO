package com.example.transaction_service.mapper;

import com.example.transaction_service.dto.ClaimDto;
import com.example.transaction_service.entity.Claim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface IClaimMapper {
    @Mapping(source = "user.userId", target = "userId")
    ClaimDto  toDto (Claim claim);
    @Mapping(source = "userId", target = "user.userId")
    Claim toEntity (ClaimDto claimDto);

    List<ClaimDto> toDto (List<Claim>claims);
    List<Claim> toEntity(List<ClaimDto>claimDtos);
}
