package com.example.transaction_service.entitie;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Bank implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long bankId;
    private String bankName;
    private  String address;
    private  String telephone;
    private String email;
    private String webSite;

    @OneToMany (mappedBy = "bank" ,cascade = CascadeType.ALL)
    private List<BankAccount> bankAccounts;


}
