package com.sawo.PasswordWallet.exception_handler;

public class IncorrectCredentialsException extends RuntimeException {

    public IncorrectCredentialsException(String message) {
        super(message);
    }
}
