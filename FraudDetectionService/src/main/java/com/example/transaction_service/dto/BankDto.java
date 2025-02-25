package com.example.transaction_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@AllArgsConstructor
@Getter
@Setter
public class BankDto {

    private Long bankId;
    private String bankName;
    private  String address;
    private  String telephone;
    private String email;
    private String webSite;


}
