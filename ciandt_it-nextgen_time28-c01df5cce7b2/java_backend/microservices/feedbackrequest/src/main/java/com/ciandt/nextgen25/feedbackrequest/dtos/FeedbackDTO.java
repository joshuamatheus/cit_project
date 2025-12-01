package com.ciandt.nextgen25.feedbackrequest.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public record FeedbackDTO(
    UUID id,
    Long requesterId,
    @JsonFormat(pattern="dd/MM/yyyy")
    LocalDateTime createdAt,
    String status
) {}