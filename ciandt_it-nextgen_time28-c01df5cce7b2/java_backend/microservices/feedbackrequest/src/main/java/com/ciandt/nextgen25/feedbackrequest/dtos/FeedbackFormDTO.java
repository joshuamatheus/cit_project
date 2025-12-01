package com.ciandt.nextgen25.feedbackrequest.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public record FeedbackFormDTO(
    UUID id,
    List<String> questions,
    List<String> appraisers,
    Map<String, String> appraiserStatus,
    Long requesterId,
    LocalDateTime createdAt
) {
    public FeedbackFormDTO {
        questions = questions != null ? questions : new ArrayList<>();
        appraisers = appraisers != null ? appraisers : new ArrayList<>();
    }
}