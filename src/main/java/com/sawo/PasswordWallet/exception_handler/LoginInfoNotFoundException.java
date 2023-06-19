package com.sawo.PasswordWallet.exception_handler;

public class LoginInfoNotFoundException extends RuntimeException {

    public LoginInfoNotFoundException(String message) {
        super(message);
    }
}
