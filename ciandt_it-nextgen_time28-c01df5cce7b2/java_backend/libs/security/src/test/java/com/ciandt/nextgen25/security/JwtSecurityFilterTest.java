package com.ciandt.nextgen25.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.ciandt.nextgen25.security.JwtSecurityFilterTest.TestController;
import com.ciandt.nextgen25.security.annotation.EnableJwtSecurity;
import com.ciandt.nextgen25.security.annotation.HasType;
import com.ciandt.nextgen25.security.entity.LoggedUser;
import com.ciandt.nextgen25.security.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;

@WebMvcTest({TestController.class})
@EnableJwtSecurity
public class JwtSecurityFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenService jwtTokenService;

    private String validToken;

    @BeforeEach
    public void setup() throws JsonProcessingException, IllegalArgumentException, JWTCreationException {
        // Gerar um token válido
        validToken = jwtTokenService.generateToken(new LoggedUser(
            1L,
            "John Doe", 
            "john@doe.com", 
            "PDM",
            1L
        ));
    }

    @Test
    public void testFilterWithValidToken() throws Exception {
        mockMvc.perform(get("/protected-resource")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Access granted"));
    }

    @Test
    public void testFilterWithInvalidToken() throws Exception {
        mockMvc.perform(get("/protected-resource")
                .header("Authorization", "Bearer invalid.token"))
                .andExpect(status().isUnauthorized());
    }

    // @Test
    public void testFilterWithoutToken() throws Exception {
        mockMvc.perform(get("/protected-resource"))
                .andExpect(status().isUnauthorized());
    }

    // Classe de configuração
    @Configuration
    public static class TestConfig {

        @Bean
        public TestController testController() {
            return new TestController();
        }
    }

    // Controlador Fictício
    @RestController
    public static class TestController {

        @GetMapping("/protected-resource")
        @HasType("PDM")
        public ResponseEntity<?> protectedResource() {
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Access granted"));
        }
    }
}