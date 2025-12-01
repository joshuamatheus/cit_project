package com.ciandt.nextgen25.feedbackrequest.dtos;

import jakarta.validation.constraints.*;

import java.util.List;

public record CreateFeedbackRequestDTO(

    @NotEmpty(message = "The question list cannot be empty")
    List<@NotBlank(message = "Question text cannot be blank") String> questions,

    @NotEmpty(message = "The appraiser list cannot be empty")
    List<@NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String> appraiserEmails

) {}
