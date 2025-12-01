package com.ciandt.nextgen25.feedbackrequest.exceptions;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException with 404 status")
    void shouldHandleResourceNotFoundExceptionWith404Status() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFound(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("Resource not found", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    @DisplayName("Should handle ValidationException with 400 status")
    void shouldHandleValidationExceptionWith400Status() {
        // Arrange
        ValidationException ex = new ValidationException("Validation failed");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("Validation failed", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle IllegalStateOperationException with 409 status")
    void shouldHandleIllegalStateOperationExceptionWith409Status() {
        // Arrange
        IllegalStateOperationException ex = new IllegalStateOperationException("Illegal state");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleStateConflict(ex);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().status());
        assertEquals("Illegal state", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle AccessDeniedException with 403 status")
    void shouldHandleAccessDeniedExceptionWith403Status() {
        // Arrange
        AccessDeniedException ex = new AccessDeniedException("Access denied");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleAccessDenied(ex);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(403, response.getBody().status());
        assertEquals("Access denied", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle generic Exception with 500 status and error reference")
    void shouldHandleGenericExceptionWith500StatusAndErrorReference() {
        // Arrange
        Exception ex = new RuntimeException("Something went wrong");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleInternalServerError(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().status());
        assertTrue(response.getBody().message().contains("unexpected error"));
    }
}
