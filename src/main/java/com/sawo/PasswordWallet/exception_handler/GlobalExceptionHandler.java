package com.sawo.PasswordWallet.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<ErrorMessage> handleServiceException(Exception exception) {
        return new ResponseEntity<>(new ErrorMessage(
                LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleUserNotFound(Exception exception) {
        return new ResponseEntity<>(new ErrorMessage(
                LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MasterPasswordsNotMatching.class})
    public ResponseEntity<ErrorMessage> handleMasterPasswordNotMatching(Exception exception) {
        return new ResponseEntity<>(new ErrorMessage(
                LocalDateTime.now(), HttpStatus.CONFLICT.value(), exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UserNotLoggedInException.class})
    public ResponseEntity<ErrorMessage> handleUserNotLoggedIn(Exception exception) {
        return new ResponseEntity<>(new ErrorMessage(
                LocalDateTime.now(), HttpStatus.CONFLICT.value(), exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({IncorrectCredentialsException.class})
    public ResponseEntity<ErrorMessage> handleIncorrectCredentials(Exception exception) {
        return new ResponseEntity<>(new ErrorMessage(
                LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UsernameIsTakenException.class})
    public ResponseEntity<ErrorMessage> handleUsernameIsTaken(Exception exception) {
        return new ResponseEntity<>(new ErrorMessage(
                LocalDateTime.now(), HttpStatus.CONFLICT.value(), exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UnsuccessfulLoginThreshold.class})
    public ResponseEntity<ErrorMessage> handleUnsuccessfulLoginThreshold(Exception exception) {
        return new ResponseEntity<>(new ErrorMessage(
                LocalDateTime.now(), HttpStatus.TOO_EARLY.value(), exception.getMessage()), HttpStatus.TOO_EARLY);
    }
}
