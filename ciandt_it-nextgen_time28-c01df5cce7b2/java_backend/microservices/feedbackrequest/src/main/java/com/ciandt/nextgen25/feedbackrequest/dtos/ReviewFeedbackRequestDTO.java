package com.ciandt.nextgen25.feedbackrequest.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewFeedbackRequestDTO {
    @NotNull(message = "Approval status is required")
    Boolean approved;

    @Size(max = 500, message = "Reject message cannot exceed 500 characters")
    String rejectMessage;

    @AssertTrue(message = "Validation failed for feedback request")
    private boolean isValid() {
        if (Boolean.TRUE.equals(approved)) {
            return rejectMessage == null || rejectMessage.trim().isEmpty();
        } else {
            return rejectMessage != null && !rejectMessage.trim().isEmpty();
        }
    }
}
