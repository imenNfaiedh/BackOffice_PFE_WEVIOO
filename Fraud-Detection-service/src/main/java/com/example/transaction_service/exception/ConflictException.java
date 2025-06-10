package com.example.transaction_service.exception;

import org.apache.hc.core5.http.HttpStatus;

public class ConflictException  extends RuntimeException{


    public  int code;
    public ConflictException(String message) {

    }
}
