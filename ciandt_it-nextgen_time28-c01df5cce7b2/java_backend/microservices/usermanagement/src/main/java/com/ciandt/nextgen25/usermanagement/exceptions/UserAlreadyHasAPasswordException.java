package com.ciandt.nextgen25.usermanagement.exceptions;

public class UserAlreadyHasAPasswordException extends RuntimeException {
    public UserAlreadyHasAPasswordException(String message) {
        super(message);
    }
}
