package com.example.transaction_service.entitie;

import com.example.transaction_service.enumeration.TypeBankAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)

    private Long bankAccountId;
    private Long accountNumber;
    private Date openingDate;
    private double balance ;

    @Enumerated(EnumType.STRING)
    private TypeBankAccount typeBankAccount;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany (mappedBy = "bankAccount")
    private List<BankCard> bankCards;


    @OneToMany(mappedBy = "bankAccount")
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
