package com.ciandt.nextgen25.usermanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FirstUserValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FirstUserValidation {
    String message() default "Invalid user data.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}