package com.sawo.PasswordWallet.exception_handler;

//@ResponseStatus(value = HttpStatus.FORBIDDEN, reason="...")
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
