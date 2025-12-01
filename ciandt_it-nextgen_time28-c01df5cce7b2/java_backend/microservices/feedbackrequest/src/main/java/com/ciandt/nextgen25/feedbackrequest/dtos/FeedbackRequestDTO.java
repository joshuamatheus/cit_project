package com.ciandt.nextgen25.feedbackrequest.dtos;

import com.ciandt.nextgen25.feedbackrequest.entity.Appraiser;
import com.ciandt.nextgen25.feedbackrequest.entity.FeedbackRequest;
import com.ciandt.nextgen25.feedbackrequest.entity.Question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record FeedbackRequestDTO(
        UUID id,
        Long requesterId,
        LocalDateTime createdAt,
        List<String> questions,
        List<String> appraiserEmails
) {
    public FeedbackRequestDTO(FeedbackRequest entity) {
        this(
            entity.getId(),
            entity.getRequesterId(),
            entity.getCreatedAt(),
            entity.getQuestions().stream().map(Question::getText).toList(),
            entity.getAppraisers().stream().map(Appraiser::getEmail).toList()
        );
    }
}
