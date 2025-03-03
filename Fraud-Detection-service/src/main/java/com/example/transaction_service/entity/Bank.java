package com.example.transaction_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "FDS001T_BANK")
public class Bank implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "FDS001_BANK_ID")
    private Long bankId;
    @Column(name = "FDS001_BANK_NAME")
    private String bankName;
    @Column(name = "FDS001_ADDRESS")
    private String address;
    @Column(name = "FDS001_TELEPHONE")
    private String telephone;
    @Column(name = "FDS001_EMAIL")
    private String email;
    @Column(name = "FDS001_WEB_SITE")
    private String webSite;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
    private List<BankAccount> bankAccounts;


}
