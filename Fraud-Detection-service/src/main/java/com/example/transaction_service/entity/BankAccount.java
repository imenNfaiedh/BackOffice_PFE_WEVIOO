package com.example.transaction_service.entity;

import com.example.transaction_service.enumeration.TypeBankAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FDS002T_BANK_ACCOUNT")
@ToString
public class BankAccount implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)

   @Column(name = "FDS002_BANK_ACCOUNT_ID")
    private Long bankAccountId;
   @Column(name = "FDS002_ACCOUNT_NUMBER")
    private Long accountNumber;
   @Column(name = "FDS002_OPENING_DATE")
    private Date openingDate;
   @Column(name = "FDS002_BALANCE")
    private BigDecimal balance ;

    @Column(name = "fraud_count")
    private Integer fraudCount = 0;

    @Column(name = "is_blocked")
    private Boolean isBlocked = false;


    @Enumerated(EnumType.STRING)
    private TypeBankAccount typeBankAccount;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    @JsonIgnore
    private Bank bank;

    @OneToMany (mappedBy = "bankAccount")
    private List<BankCard> bankCards;


    @OneToMany(mappedBy = "bankAccount")
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
