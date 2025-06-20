package com.example.transaction_service.entity;

import com.example.transaction_service.enumeration.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "FDS005_CIN")
    private String cin;

    @Column(name = "FDS005_ADDRESS")
    private String address;
    @Column(name = "FDS005_USERNAME")
    private String userName;
    @Column(name = "FDS005_URL_IMAGE")
    private String profileImageUrl;


    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany (mappedBy = "user")
    @JsonIgnore
    private List<BankAccount> bankAccounts;

    @OneToMany (mappedBy = "user")
    @JsonIgnore
    private List<Claim> claims;


}
