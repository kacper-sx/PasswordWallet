package com.sawo.PasswordWallet.exception_handler;

public class MasterPasswordsNotMatching extends RuntimeException {
    public MasterPasswordsNotMatching(String message) {
        super(message);
    }
}
