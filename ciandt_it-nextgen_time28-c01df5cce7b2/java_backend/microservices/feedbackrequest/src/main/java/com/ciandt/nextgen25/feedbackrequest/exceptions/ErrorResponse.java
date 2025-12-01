package com.ciandt.nextgen25.feedbackrequest.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {}
