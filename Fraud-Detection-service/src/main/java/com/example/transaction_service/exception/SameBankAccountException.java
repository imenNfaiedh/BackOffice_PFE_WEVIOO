package com.example.transaction_service.exception;

public class SameBankAccountException extends RuntimeException {
    public SameBankAccountException(String message) {
        super(message);
    }}
