package com.ciandt.nextgen25.usermanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import com.ciandt.nextgen25.usermanagement.dto.ErrorResponse;
import com.ciandt.nextgen25.usermanagement.exceptions.EntityNotFoundException;
import com.ciandt.nextgen25.usermanagement.exceptions.GlobalExceptionHandler;
import com.ciandt.nextgen25.usermanagement.exceptions.UserAlreadyExistsException;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Optional.ofNullable(response.getBody()).ifPresent(body -> {
            assertEquals("Entity not found", body.message());
            assertEquals(HttpStatus.NOT_FOUND.value(), body.statusCode());
        });
    }

    @Test
    public void testHandleUserAlreadyExistsException() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("User already exists");
        ResponseEntity<?> response = globalExceptionHandler.handleConflictExceptions(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Optional.ofNullable(response.getBody()).ifPresent(body -> {
            assertTrue(body instanceof ErrorResponse);
            ErrorResponse errorResponse = (ErrorResponse) body;
            assertEquals("User already exists", errorResponse.message());
            assertEquals(HttpStatus.CONFLICT.value(), errorResponse.statusCode());
        });
    }

    @Test
    public void testHandleMethodArgumentNotValidException() {
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");
        when(bindingResult.getFieldErrors()).thenReturn(java.util.Collections.singletonList(fieldError));

        ResponseEntity<Object> response = globalExceptionHandler.handleValidationExceptions(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Optional.ofNullable(response.getBody()).ifPresent(body -> {
            assertTrue(body instanceof Map);
            @SuppressWarnings("unchecked")
            Map<String, Object> responseBody = (Map<String, Object>) body;
            assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.get("status"));
            assertEquals("Validation failed", responseBody.get("message"));
            
            @SuppressWarnings("unchecked")
            Map<String, String> errors = (Map<String, String>) responseBody.get("errors");
            assertNotNull(errors);
            assertEquals("error message", errors.get("fieldName"));
        });
    }

    @Test
    public void testHandleGenericException() {
        Exception ex = new RuntimeException("Unexpected error");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(ex);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Optional.ofNullable(response.getBody()).ifPresent(body -> {
            assertEquals("An unexpected error occurred", body.message());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.statusCode());
        });
    }
}