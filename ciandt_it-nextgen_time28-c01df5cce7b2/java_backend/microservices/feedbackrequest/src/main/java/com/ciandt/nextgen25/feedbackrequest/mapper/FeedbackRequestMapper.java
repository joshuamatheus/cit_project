package com.ciandt.nextgen25.feedbackrequest.mapper;

import com.ciandt.nextgen25.feedbackrequest.dtos.AnswerDTO;
import com.ciandt.nextgen25.feedbackrequest.dtos.AppraiserDTO;
import com.ciandt.nextgen25.feedbackrequest.dtos.FeedbackDTO;
import com.ciandt.nextgen25.feedbackrequest.dtos.FeedbackRequestCiandTDTO;
import com.ciandt.nextgen25.feedbackrequest.dtos.QuestionDTO;
import com.ciandt.nextgen25.feedbackrequest.entity.Appraiser;
import com.ciandt.nextgen25.feedbackrequest.entity.FeedbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting between FeedbackRequest entities and DTOs
 */
@Component
public class FeedbackRequestMapper {

    private final AppraiserMapper appraiserMapper;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;

    @Autowired
    public FeedbackRequestMapper(
            AppraiserMapper appraiserMapper,
            QuestionMapper questionMapper,
            AnswerMapper answerMapper) {
        this.appraiserMapper = appraiserMapper;
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
    }

    /**
     * Converts a FeedbackRequest entity to a FeedbackRequestDTO
     *
     * @param feedbackRequest The entity to convert
     * @return A FeedbackRequestDTO with all corresponding fields
     */
    public FeedbackRequestCiandTDTO toDTO(FeedbackRequest feedbackRequest) {
        if (feedbackRequest == null) {
            return null;
        }

        FeedbackRequestCiandTDTO dto = new FeedbackRequestCiandTDTO();
        dto.setId(feedbackRequest.getId());
        dto.setRequesterId(feedbackRequest.getRequesterId());
        dto.setCreatedAt(feedbackRequest.getCreatedAt());
        dto.setApprovedAt(feedbackRequest.getApprovedAt());
        dto.setRejectedAt(feedbackRequest.getRejectedAt());
        dto.setEditedAt(feedbackRequest.getEditedAt());

        // Map appraisers using the dedicated mapper
        List<AppraiserDTO> appraiserDTOs = feedbackRequest.getAppraisers().stream()
                .map(appraiserMapper::toDTO)
                .collect(Collectors.toList());
        dto.setAppraisers(appraiserDTOs);

        // Map questions using the dedicated mapper
        List<QuestionDTO> questionDTOs = feedbackRequest.getQuestions().stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
        dto.setQuestions(questionDTOs);

        // Map answers using the dedicated mapper
        List<AnswerDTO> answerDTOs = feedbackRequest.getAnswers().stream()
                .map(answerMapper::toDTO)
                .collect(Collectors.toList());
        dto.setAnswers(answerDTOs);

        // Set status flags
        dto.setApproved(feedbackRequest.isApproved());
        dto.setRejected(feedbackRequest.isRejected());
        dto.setExpired(feedbackRequest.isExpired());

        return dto;
    }

    /**
     * Converts a FeedbackRequest entity to a simplified FeedbackDTO
     *
     * @param feedbackRequest The entity to convert
     * @return A simplified FeedbackDTO with essential information
     */
    public FeedbackDTO toFeedbackDTO(FeedbackRequest feedbackRequest) {
        if (feedbackRequest == null) {
            return null;
        }

        String status = determineStatus(feedbackRequest);

        return new FeedbackDTO(
                feedbackRequest.getId(),
                feedbackRequest.getRequesterId(),
                feedbackRequest.getCreatedAt(),
                status
        );
    }

    /**
     * Creates a new FeedbackRequest entity from DTO data
     * Note: This doesn't set the ID as it's typically generated
     *
     * @param dto The DTO containing request data
     * @return A new FeedbackRequest entity
     */
    public FeedbackRequest toEntity(FeedbackRequestCiandTDTO dto) {
        if (dto == null) {
            return null;
        }

        FeedbackRequest entity = new FeedbackRequest(dto.getRequesterId());

        // Custom setters for dates if they need to be preserved from the DTO
        if (dto.getCreatedAt() != null) {
            entity.setCreatedAt(dto.getCreatedAt());
        }

        if (dto.getApprovedAt() != null) {
            entity.setApprovedAt(dto.getApprovedAt());
        }

        if (dto.getRejectedAt() != null) {
            entity.setRejectedAt(dto.getRejectedAt());
        }

        if (dto.getEditedAt() != null) {
            entity.setEditedAt(dto.getEditedAt());
        }

        // Add questions and appraisers
        if (dto.getQuestions() != null) {
            dto.getQuestions().forEach(q -> entity.addQuestion(q.getText()));
        }

        if (dto.getAppraisers() != null) {
            dto.getAppraisers().forEach(a -> entity.addAppraiser(a.getEmail()));
        }

        // Note: We don't add answers here because they should be submitted
        // through the appropriate method after the request is approved

        return entity;
    }

    /**
     * Updates an existing FeedbackRequest entity with data from a DTO
     *
     * @param entity The existing entity to update
     * @param dto The DTO with updated data
     */
    public void updateEntityFromDTO(FeedbackRequest entity, FeedbackRequestCiandTDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        // Only update requester ID if provided
        if (dto.getRequesterId() != null) {
            entity.setRequesterId(dto.getRequesterId());
        }

        // Handle appraisers - remove and re-add to avoid duplicate checking issues
        if (dto.getAppraisers() != null) {
            // Clear existing appraisers
            List<String> existingEmails = entity.getAppraisers().stream()
                    .map(Appraiser::getEmail)
                    .collect(Collectors.toList());

            for (String email : existingEmails) {
                entity.removeAppraiser(email);
            }

            // Add new appraisers
            dto.getAppraisers().forEach(a -> entity.addAppraiser(a.getEmail()));
        }

        // Handle questions - replace entirely if provided
        if (dto.getQuestions() != null) {
            // Remove existing questions in reverse order to avoid index shifting issues
            for (int i = entity.getQuestions().size() - 1; i >= 0; i--) {
                entity.removeQuestion(i);
            }

            // Add new questions
            dto.getQuestions().forEach(q -> entity.addQuestion(q.getText()));
        }

        // Mark as edited
        entity.markAsEdited();
    }

    /**
     * Determines the status of a feedback request
     *
     * @param request The feedback request
     * @return Status as a string (Approved, Rejected, Expired, Edited, or Pending)
     */
    private String determineStatus(FeedbackRequest request) {
        if (request.isExpired()) {
            return "Expired";
        }
        if (request.isApproved()) {
            return "Approved";
        }
        if (request.isRejected()) {
            return "Rejected";
        }
        if (request.isEdited()) {
            return "Edited";
        }
        return "Pending";
    }

    /**
     * Converts a list of FeedbackRequest entities to a list of FeedbackDTOs
     *
     * @param requests The list of entities to convert
     * @return A list of FeedbackDTOs
     */
    public List<FeedbackDTO> toFeedbackDTOList(List<FeedbackRequest> requests) {
        if (requests == null) {
            return List.of();
        }

        return requests.stream()
                .map(this::toFeedbackDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of FeedbackRequest entities to a list of FeedbackRequestDTOs
     *
     * @param requests The list of entities to convert
     * @return A list of FeedbackRequestDTOs
     */
    public List<FeedbackRequestCiandTDTO> toDTOList(List<FeedbackRequest> requests) {
        if (requests == null) {
            return List.of();
        }

        return requests.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}