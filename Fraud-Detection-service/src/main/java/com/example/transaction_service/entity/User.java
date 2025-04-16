package com.example.transaction_service.entity;

import com.example.transaction_service.enumeration.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@NoArgsConstructor

@Setter
@Getter
@Table(name = "FDS005T_USER")
public class User implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    @Column(name = "FDS005_USER_ID")
    private Long userId;
    @Column(name = "FDS005_FIRST_NAME")
    private String firstName;
    @Column(name = "FDS005_LAST_NAME")
    private String lastName;
    @Column(name = "FDS005_EMAIL")
    private String email;
    @Column(name = "FDS005_TEL")
    private String tel;
    @Column(name = "FDS005_KEYCLOAK_ID")
    private String keycloakId;

    @Column(name = "FDS005_SUSPICIOUS_ACTIVITY")
    private Boolean suspicious_activity;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany (mappedBy = "user")
    private List<BankAccount> bankAccounts;



}
