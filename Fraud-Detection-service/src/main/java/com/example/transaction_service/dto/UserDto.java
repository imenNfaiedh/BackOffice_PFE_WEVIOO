package com.example.transaction_service.dto;

import com.example.transaction_service.enumeration.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String tel;
    private Boolean suspicious_activity;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
