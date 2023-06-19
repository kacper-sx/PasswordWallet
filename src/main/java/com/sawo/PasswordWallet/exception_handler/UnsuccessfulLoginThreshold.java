package com.sawo.PasswordWallet.exception_handler;

public class UnsuccessfulLoginThreshold extends RuntimeException {

    public UnsuccessfulLoginThreshold(String message) {
        super(message);
    }
}
