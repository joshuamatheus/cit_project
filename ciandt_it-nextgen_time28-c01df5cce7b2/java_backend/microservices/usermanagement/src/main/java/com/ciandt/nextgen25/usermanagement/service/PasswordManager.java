package com.ciandt.nextgen25.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordManager {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordManager(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(String username, String password) {
        // Criptografa a senha usando BCrypt
        String encryptedPassword = passwordEncoder.encode(password);

        return encryptedPassword;
    }

    public boolean validateUser(String rawPassword, String storedEncryptedPassword) {
        // Verifica se a senha fornecida corresponde à senha criptografada armazenada
        return passwordEncoder.matches(rawPassword, storedEncryptedPassword);
    }
}