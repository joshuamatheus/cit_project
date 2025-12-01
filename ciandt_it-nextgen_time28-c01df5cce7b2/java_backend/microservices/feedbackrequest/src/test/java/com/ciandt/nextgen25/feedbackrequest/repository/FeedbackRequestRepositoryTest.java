package com.ciandt.nextgen25.feedbackrequest.repository;

import com.ciandt.nextgen25.feedbackrequest.entity.FeedbackRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FeedbackRequestRepositoryTest {

    @Autowired
    private FeedbackRequestRepository feedbackRequestRepository;

    private static final Long REQUESTER_ID_WITH_DATA = 101L;
    private static final Long REQUESTER_ID_WITHOUT_DATA = 999L;
    private static final String APPRAISER_EMAIL_WITH_DATA = "joao.silva@example.com";
    private static final String APPRAISER_EMAIL_WITHOUT_DATA = "nonexistent@example.com";
    private static final UUID EXISTING_FEEDBACK_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID NON_EXISTING_FEEDBACK_ID = UUID.fromString("550e8400-e29b-41d4-a716-000000000000");

    @Test
    public void testFindByRequesterId() {
        // Act
        List<FeedbackRequest> requests = feedbackRequestRepository.findByRequesterId(REQUESTER_ID_WITH_DATA);

        // Assert
        assertEquals(2, requests.size());
    }

    @Test
    public void testFindById() {
        // ID do feedback request completo
        UUID id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        // Act
        FeedbackRequest request = feedbackRequestRepository.findById(id).orElse(null);

        // Assert
        assertNotNull(request);
        assertEquals(REQUESTER_ID_WITH_DATA, request.getRequesterId());
    }

    @Test
    @DisplayName("Should find feedback request by ID when exists")
    void shouldFindFeedbackRequestById() {
        // Act
        Optional<FeedbackRequest> result = feedbackRequestRepository.findById(EXISTING_FEEDBACK_ID);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(EXISTING_FEEDBACK_ID, result.get().getId());
    }

    @Test
    @DisplayName("Should return empty when finding feedback request by ID that doesn't exist")
    void shouldReturnEmptyWhenFindingNonExistingFeedbackRequest() {
        // Act
        Optional<FeedbackRequest> result = feedbackRequestRepository.findById(NON_EXISTING_FEEDBACK_ID);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should save new feedback request")
    void shouldSaveNewFeedbackRequest() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(201L);
        feedbackRequest.addQuestion("Test Question 1");
        feedbackRequest.addQuestion("Test Question 2");
        feedbackRequest.addQuestion("Test Question 3");
        feedbackRequest.addAppraiser("test.user@example.com");

        // Act
        FeedbackRequest savedFeedbackRequest = feedbackRequestRepository.save(feedbackRequest);

        // Assert
        assertNotNull(savedFeedbackRequest.getId());
        assertNotNull(savedFeedbackRequest.getCreatedAt());
        assertEquals(201L, savedFeedbackRequest.getRequesterId());
        assertEquals(3, savedFeedbackRequest.getQuestions().size());
        assertEquals(1, savedFeedbackRequest.getAppraisers().size());
    }

    @Test
    @DisplayName("Should find feedback requests by requester ID")
    void shouldFindFeedbackRequestsByRequesterId() {
        // Act
        List<FeedbackRequest> results = feedbackRequestRepository.findByRequesterId(REQUESTER_ID_WITH_DATA);

        // Assert
        assertFalse(results.isEmpty());
        results.forEach(feedbackRequest ->
                assertEquals(REQUESTER_ID_WITH_DATA, feedbackRequest.getRequesterId())
        );
    }

    @Test
    @DisplayName("Should return empty list when finding feedback requests by non-existing requester ID")
    void shouldReturnEmptyListWhenFindingByNonExistingRequesterId() {
        // Act
        List<FeedbackRequest> results = feedbackRequestRepository.findByRequesterId(REQUESTER_ID_WITHOUT_DATA);

        // Assert
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should find feedback requests by appraiser email")
    void shouldFindFeedbackRequestsByAppraiserEmail() {
        // Act
        List<FeedbackRequest> results = feedbackRequestRepository.findByAppraiserEmail(APPRAISER_EMAIL_WITH_DATA);

        // Assert
        assertFalse(results.isEmpty());
        results.forEach(feedbackRequest -> {
            boolean containsAppraiser = feedbackRequest.getAppraisers().stream()
                    .anyMatch(appraiser -> appraiser.getEmail().equals(APPRAISER_EMAIL_WITH_DATA));
            assertTrue(containsAppraiser);
        });
    }

    @Test
    @DisplayName("Should return empty list when finding feedback requests by non-existing appraiser email")
    void shouldReturnEmptyListWhenFindingByNonExistingAppraiserEmail() {
        // Act
        List<FeedbackRequest> results = feedbackRequestRepository.findByAppraiserEmail(APPRAISER_EMAIL_WITHOUT_DATA);

        // Assert
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should find all approved feedback requests")
    void shouldFindAllCompletedFeedbackRequests() {
        // Act
        List<FeedbackRequest> results = feedbackRequestRepository.findAllCompleted();

        // Assert
        assertFalse(results.isEmpty());
        results.forEach(feedbackRequest -> {
            assertNotNull(feedbackRequest.getApprovedAt(), "Approved feedback requests should have approvedAt timestamp");
        });
    }

    @Test
    @DisplayName("Should find all pending feedback requests")
    void shouldFindAllPendingFeedbackRequests() {
        // Act
        List<FeedbackRequest> results = feedbackRequestRepository.findAllPending();

        // Assert
        assertFalse(results.isEmpty());
        results.forEach(feedbackRequest -> {
            assertNull(feedbackRequest.getApprovedAt(), "Pending feedback requests should NOT have approvedAt timestamp");

            // Make sure the feedback request isn't rejected either
            assertNull(feedbackRequest.getRejectedAt(), "Pending feedback requests should NOT be rejected");
        });
    }

    @Test
    @DisplayName("Should delete feedback request")
    void shouldDeleteFeedbackRequest() {
        // Arrange
        Optional<FeedbackRequest> feedbackRequestOptional = feedbackRequestRepository.findById(EXISTING_FEEDBACK_ID);
        assertTrue(feedbackRequestOptional.isPresent());

        // Act
        feedbackRequestRepository.delete(feedbackRequestOptional.get());
        Optional<FeedbackRequest> deletedFeedbackRequest = feedbackRequestRepository.findById(EXISTING_FEEDBACK_ID);

        // Assert
        assertTrue(deletedFeedbackRequest.isEmpty());
    }

    @Test
    @DisplayName("Should count feedback requests")
    void shouldCountFeedbackRequests() {
        // Act
        long count = feedbackRequestRepository.count();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    @DisplayName("Should find all feedback requests")
    void shouldFindAllFeedbackRequests() {
        // Act
        List<FeedbackRequest> results = feedbackRequestRepository.findAll();

        // Assert
        assertFalse(results.isEmpty());
    }
}