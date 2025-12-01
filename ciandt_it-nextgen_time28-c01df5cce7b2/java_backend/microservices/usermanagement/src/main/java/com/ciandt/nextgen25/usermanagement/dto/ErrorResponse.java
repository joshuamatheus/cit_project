package com.ciandt.nextgen25.usermanagement.dto;

public record ErrorResponse (
    String message,
    Integer statusCode
) {}
