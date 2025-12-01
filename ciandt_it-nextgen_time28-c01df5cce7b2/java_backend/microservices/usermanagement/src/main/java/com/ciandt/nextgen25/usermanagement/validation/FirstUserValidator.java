package com.ciandt.nextgen25.usermanagement.validation;

import com.ciandt.nextgen25.usermanagement.entity.User;
import com.ciandt.nextgen25.usermanagement.entity.UserType;
import com.ciandt.nextgen25.usermanagement.repository.UserRepository;
import com.ciandt.nextgen25.usermanagement.dto.UserRegisterDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Log4j2
@Component
public class FirstUserValidator implements Validator, ConstraintValidator<FirstUserValidation, UserRegisterDTO> {

    private final UserRepository repository;

    @Autowired
    public FirstUserValidator(UserRepository repository) {
        this.repository = repository;
        log.info("FirstUserValidator created with repository: {}", repository != null ? repository.getClass().getName() : "null");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(UserRegisterDTO userRegisterDTO, ConstraintValidatorContext context) {
        try {
            log.info("Starting validation for userRegisterDTO: {}",
                    userRegisterDTO != null ? userRegisterDTO.email() : "null");

            if (userRegisterDTO == null) {
                log.info("UserRegisterDTO is null, skipping validation");
                return true;
            }

            // Null-safety verification for fields
            String email = userRegisterDTO.email();
            UserType type = userRegisterDTO.type();
            String pdmEmail = userRegisterDTO.pdmEmail();

            log.info("Validating - Email: {}, Type: {}, PDM Email: {}",
                    email, type, pdmEmail);

            // Check if repository is properly injected
            if (repository == null) {
                log.error("UserRepository was not injected properly");
                addConstraintViolation(context, "Server configuration error. Please contact support.", "");
                return false;
            }

            boolean isFirstUser = false;
            try {
                isFirstUser = repository.count() == 1;
                log.info("Is first user: {}", isFirstUser);
            } catch (Exception e) {
                log.error("Error checking if first user", e);
                addConstraintViolation(context, "Error checking user count: " + e.getMessage(), "");
                return false;
            }

            if (isFirstUser) {
                // First user validation
                if (type != UserType.PDM) {
                    log.info("First user must be PDM but was: {}", type);
                    addConstraintViolation(context, "O tipo deve ser 'PDM' para o primeiro usuário.", "type");
                    return false;
                }

                if (pdmEmail != null && !pdmEmail.isEmpty()) {
                    log.info("First user should not have PDM email but had: {}", pdmEmail);
                    addConstraintViolation(context, "O campo pdmEmail deve estar vazio para o primeiro usuário.", "pdmEmail");
                    return false;
                }
            } else {
                // Subsequent user validation
                if (pdmEmail == null || pdmEmail.isEmpty()) {
                    log.info("Subsequent user missing PDM email");
                    addConstraintViolation(context, "The pdmEmail field must be filled out for subsequent users", "pdmEmail");
                    return false;
                } else {
                    log.info("Checking PDM email: {}", pdmEmail);

                    try {
                        Optional<User> pdmUserOptional = repository.findByEmail(pdmEmail);

                        if (pdmUserOptional.isEmpty()) {
                            log.info("PDM user not found for email: {}", pdmEmail);
                            addConstraintViolation(context, "User with email " + pdmEmail + " not found", "pdmEmail");
                            return false;
                        }

                        User pdmUser = pdmUserOptional.get();

                        if (pdmUser.getType() == null) {
                            log.warn("PDM user has null type: {}", pdmEmail);
                            addConstraintViolation(context, "Referenced user has invalid type", "pdmEmail");
                            return false;
                        }

                        if (!UserType.PDM.equals(pdmUser.getType())) {
                            log.info("User is not a PDM: {} (type: {})", pdmEmail, pdmUser.getType());
                            addConstraintViolation(context, "User with email " + pdmEmail + " is not a PDM", "pdmEmail");
                            return false;
                        }
                    } catch (Exception e) {
                        log.error("Error checking PDM user", e);
                        addConstraintViolation(context, "Error checking PDM: " + e.getMessage(), "pdmEmail");
                        return false;
                    }
                }
            }

            log.info("Validation passed");
            return true;
        } catch (Exception e) {
            log.error("Unexpected exception during validation", e);
            addConstraintViolation(context, "Internal validation error: " + e.getMessage(), "");
            // This is important - DO NOT THROW the exception, just return false
            return false;
        }
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message, String property) {
        try {
            log.debug("Adding constraint violation - Property: {}, Message: {}", property, message);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(property)
                    .addConstraintViolation();
        } catch (Exception e) {
            log.error("Error adding constraint violation", e);
            // If we can't add a violation, just log the error
            // Don't throw any exceptions here
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}