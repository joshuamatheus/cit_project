package com.ciandt.nextgen25.feedbackrequest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequestCiandTDTO {
    private UUID id;
    private Long requesterId;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime editedAt;
    private List<AppraiserDTO> appraisers;
    private List<QuestionDTO> questions;
    private List<AnswerDTO> answers;
    private boolean isApproved;
    private boolean isRejected;
    private boolean isExpired;
}