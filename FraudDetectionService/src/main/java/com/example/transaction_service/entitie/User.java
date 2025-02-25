package com.example.transaction_service.entitie;

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
@AllArgsConstructor
@Setter
@Getter
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String tel;
    private Boolean suspicious_activity;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany (mappedBy = "user")
    private List<BankAccount> bankAccounts;



}
