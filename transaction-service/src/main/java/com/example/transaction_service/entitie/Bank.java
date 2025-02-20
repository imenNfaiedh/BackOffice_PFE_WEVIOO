package com.example.transaction_service.entitie;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long idBank;
    private String nameBank;
    private  String address;
    private  String telephone;
    private String email;
    private String siteWeb;




}
