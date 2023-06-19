package com.sawo.PasswordWallet.exception_handler;

public class UserNotLoggedInException extends RuntimeException {

    public UserNotLoggedInException(String message) {
        super(message);
    }
}
