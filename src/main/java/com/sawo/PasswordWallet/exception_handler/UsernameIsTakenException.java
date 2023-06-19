package com.sawo.PasswordWallet.exception_handler;

public class UsernameIsTakenException extends RuntimeException {
    public UsernameIsTakenException(String message) {
        super(message);
    }
}
