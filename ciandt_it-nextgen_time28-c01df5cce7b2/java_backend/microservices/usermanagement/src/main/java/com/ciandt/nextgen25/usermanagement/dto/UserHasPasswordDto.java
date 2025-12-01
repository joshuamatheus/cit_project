package com.ciandt.nextgen25.usermanagement.dto;

public record UserHasPasswordDto(
    String name,
    Boolean hasPassword,
    String email
) {}
