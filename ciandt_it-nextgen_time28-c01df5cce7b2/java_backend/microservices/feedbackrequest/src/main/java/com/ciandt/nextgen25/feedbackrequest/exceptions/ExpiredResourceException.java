package com.ciandt.nextgen25.feedbackrequest.exceptions;

public class ExpiredResourceException extends RuntimeException {
    public ExpiredResourceException(String message) {
        super(message);
    }
}
