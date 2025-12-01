package com.ciandt.nextgen25.usermanagement.exceptions;

public record ErrorResponse (
    String message,
    Integer statusCode
) {}
