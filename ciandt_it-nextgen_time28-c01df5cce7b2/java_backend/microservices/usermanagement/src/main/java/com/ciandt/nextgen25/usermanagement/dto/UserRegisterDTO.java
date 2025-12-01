package com.ciandt.nextgen25.usermanagement.dto;

import com.ciandt.nextgen25.usermanagement.entity.PositionMap;
import com.ciandt.nextgen25.usermanagement.entity.Role;
import com.ciandt.nextgen25.usermanagement.entity.UserType;

import com.ciandt.nextgen25.usermanagement.validation.FirstUserValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@FirstUserValidation
public record UserRegisterDTO (

    @NotBlank(message = "Name cannot be empty.")
    String name,

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Email must be valid.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@ciandt\\.com$", message = "Email must be from the domain @ciandt.com")
    String email,

    @NotNull(message = "User type must be specified.")
    UserType type,

    @NotNull(message = "Position must be specified.")
    PositionMap positionMap,

    @NotNull(message = "Role must be specified.")
    Role role, 

    @Email(message = "Email must be valid.")
    @Pattern(regexp = "^$|^[A-Za-z0-9._%+-]+@ciandt\\.com$", message = "Email must be from the domain @ciandt.com")
    String pdmEmail

){}
