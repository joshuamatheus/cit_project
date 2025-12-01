package com.ciandt.nextgen25.feedbackrequest.entity;

import com.ciandt.nextgen25.feedbackrequest.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FeedbackRequestDomainTest {

    @Test
    @DisplayName("Should reject feedback request with a valid message")
    void shouldRejectFeedbackRequestWithValidMessage() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        String rejectMessage = "Not meeting requirements";

        // Act
        feedbackRequest.reject(rejectMessage);

        // Assert
        assertTrue(feedbackRequest.isRejected());
        assertEquals(rejectMessage, feedbackRequest.getRejectMessage());
        assertNotNull(feedbackRequest.getRejectedAt());
        assertFalse(feedbackRequest.isApproved());
    }

    @Test
    @DisplayName("Should throw exception when rejecting with an empty message")
    void shouldThrowExceptionWhenRejectingWithEmptyMessage() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);

        // Act & Assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> feedbackRequest.reject("")
        );

        assertEquals("Reject message cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should remove question successfully")
    void shouldRemoveQuestionSuccessfully() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        feedbackRequest.addQuestion("Question 1");
        feedbackRequest.addQuestion("Question 2");

        // Act
        feedbackRequest.removeQuestion(0);

        // Assert
        assertEquals(1, feedbackRequest.getQuestions().size());
        assertEquals("Question 2", feedbackRequest.getQuestions().get(0).getText());
    }

    @Test
    @DisplayName("Should throw exception when removing question with invalid index")
    void shouldThrowExceptionWhenRemovingQuestionWithInvalidIndex() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        feedbackRequest.addQuestion("Question 1");

        // Act & Assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> feedbackRequest.removeQuestion(1)
        );

        assertTrue(exception.getMessage().contains("index out of range"));
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when adding an internal email")
    @ValueSource(strings = {"user@ciandt.com", "user@ciandt.com.br", "test@ciandt"})
    void shouldThrowExceptionWhenAddingInternalEmail(String email) {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);

        // Act & Assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> feedbackRequest.addAppraiser(email)
        );

        assertEquals("The email cannot be from an internal employee.", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully submit answer for approved feedback")
    void shouldSuccessfullySubmitAnswerForApprovedFeedback() throws Exception {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        feedbackRequest.addQuestion("Question 1");
        feedbackRequest.addQuestion("Question 2");
        feedbackRequest.addQuestion("Question 3");
        feedbackRequest.addAppraiser("john.doe@example.com");
        feedbackRequest.approve();

        // Act - set approval date to avoid expiration issues
        String answerText = "This is my answer";
        feedbackRequest.submitAnswer("john.doe@example.com", answerText);

        // Assert
        assertEquals(1, feedbackRequest.getAnswers().size());
        assertEquals("john.doe@example.com", feedbackRequest.getAnswers().get(0).getAppraiserEmail());
        assertEquals(answerText, feedbackRequest.getAnswers().get(0).getText());
    }

    @Test
    @DisplayName("Should get appraiser answers correctly")
    void shouldGetAppraiserAnswersCorrectly() throws Exception {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        feedbackRequest.addQuestion("Question 1");
        feedbackRequest.addQuestion("Question 2");
        feedbackRequest.addQuestion("Question 3");
        feedbackRequest.addAppraiser("john@example.com");
        feedbackRequest.addAppraiser("jane@example.com");
        feedbackRequest.approve();

        // Submit answers
        feedbackRequest.submitAnswer("john@example.com", "John's answer 1");
        feedbackRequest.submitAnswer("john@example.com", "John's answer 2");
        feedbackRequest.submitAnswer("jane@example.com", "Jane's answer");

        // Act
        var johnAnswers = feedbackRequest.getAppraiserAnswers("john@example.com");
        var janeAnswers = feedbackRequest.getAppraiserAnswers("jane@example.com");

        // Assert
        assertEquals(2, johnAnswers.size());
        assertEquals(1, janeAnswers.size());
        assertEquals("John's answer 1", johnAnswers.get(0).getText());
        assertEquals("John's answer 2", johnAnswers.get(1).getText());
        assertEquals("Jane's answer", janeAnswers.get(0).getText());
    }

    @Test
    @DisplayName("Should remove appraiser and their answers")
    void shouldRemoveAppraiserAndTheirAnswers() throws Exception {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        feedbackRequest.addQuestion("Question 1");
        feedbackRequest.addQuestion("Question 2");
        feedbackRequest.addQuestion("Question 3");
        feedbackRequest.addAppraiser("john@example.com");
        feedbackRequest.addAppraiser("jane@example.com");
        feedbackRequest.approve();

        // Submit answers
        feedbackRequest.submitAnswer("john@example.com", "John's answer");
        feedbackRequest.submitAnswer("jane@example.com", "Jane's answer");

        // Act
        feedbackRequest.removeAppraiser("john@example.com");

        // Assert
        assertEquals(1, feedbackRequest.getAppraisers().size());
        assertEquals("jane@example.com", feedbackRequest.getAppraisers().get(0).getEmail());
        assertEquals(1, feedbackRequest.getAnswers().size());
        assertEquals("jane@example.com", feedbackRequest.getAnswers().get(0).getAppraiserEmail());
    }

    @Test
    @DisplayName("Should get correct appraisers status for unapproved request")
    void shouldGetCorrectAppraisersStatusForUnapprovedRequest() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        feedbackRequest.addAppraiser("john@example.com");
        feedbackRequest.addAppraiser("jane@example.com");

        // Act
        Map<String, String> status = feedbackRequest.getAppraisersWithStatus();

        // Assert
        assertEquals(2, status.size());
        assertEquals("Não Enviado", status.get("john@example.com"));
        assertEquals("Não Enviado", status.get("jane@example.com"));
    }

    @Test
    @DisplayName("Should get correct appraisers status for approved request")
    void shouldGetCorrectAppraisersStatusForApprovedRequest() throws Exception {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        feedbackRequest.addQuestion("Question 1");
        feedbackRequest.addQuestion("Question 2");
        feedbackRequest.addQuestion("Question 3");
        feedbackRequest.addAppraiser("john@example.com");
        feedbackRequest.addAppraiser("jane@example.com");
        feedbackRequest.approve();

        // Submit answer for one appraiser
        feedbackRequest.submitAnswer("john@example.com", "John's answer");

        // Act
        Map<String, String> status = feedbackRequest.getAppraisersWithStatus();

        // Assert
        assertEquals(2, status.size());
        assertEquals("Respondido", status.get("john@example.com"));
        assertEquals("Não Respondido", status.get("jane@example.com"));
    }

    @Test
    @DisplayName("Should return Em aprovação status for newly created request")
    void shouldReturnEmAprovacaoStatusForNewlyCreatedRequest() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);

        // Act
        String status = feedbackRequest.getRequestStatus();

        // Assert
        assertEquals("Em aprovação", status);
    }

    @Test
    @DisplayName("Should return Rejeitado status for rejected request")
    void shouldReturnRejeitadoStatusForRejectedRequest() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        feedbackRequest.reject("Not approved");

        // Act
        String status = feedbackRequest.getRequestStatus();

        // Assert
        assertEquals("Rejeitado", status);
    }

    @Test
    @DisplayName("Should return Expirado status for expired request")
    void shouldReturnExpiradoStatusForExpiredRequest() throws Exception {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);

        // Act - using reflection to simulate expiration
        Field createdAtField = FeedbackRequest.class.getDeclaredField("createdAt");
        createdAtField.setAccessible(true);
        createdAtField.set(feedbackRequest, LocalDateTime.now().minusMonths(4));

        String status = feedbackRequest.getRequestStatus();

        // Assert
        assertEquals("Expirado", status);
    }

    @Test
    @DisplayName("Should return Aguardando respostas status for approved request without answers")
    void shouldReturnAguardandoRespostasStatusForApprovedRequestWithoutAnswers() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        feedbackRequest.addQuestion("Question 1");
        feedbackRequest.addQuestion("Question 2");
        feedbackRequest.addQuestion("Question 3");
        feedbackRequest.addAppraiser("john@example.com");
        feedbackRequest.approve();

        // Act
        String status = feedbackRequest.getRequestStatus();

        // Assert
        assertEquals("Aguardando respostas", status);
    }

    @Test
    @DisplayName("Should return Finalizado status when all appraisers have answered")
    void shouldReturnFinalizadoStatusWhenAllAppraisersHaveAnswered() throws Exception {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        feedbackRequest.addQuestion("Question 1");
        feedbackRequest.addQuestion("Question 2");
        feedbackRequest.addQuestion("Question 3");
        feedbackRequest.addAppraiser("john@example.com");
        feedbackRequest.approve();

        // Submit answers for all questions
        feedbackRequest.submitAnswer("john@example.com", "Answer 1");
        feedbackRequest.submitAnswer("john@example.com", "Answer 2");
        feedbackRequest.submitAnswer("john@example.com", "Answer 3");

        // Act
        String status = feedbackRequest.getRequestStatus();

        // Assert
        assertEquals("Finalizado", status);
    }

    @Test
    @DisplayName("Should handle case sensitivity in appraiser email correctly")
    void shouldHandleCaseSensitivityInAppraiserEmailCorrectly() {
        // Arrange
        FeedbackRequest feedbackRequest = new FeedbackRequest(123L);
        feedbackRequest.addAppraiser("John.Doe@Example.com");

        // Act & Assert
        assertTrue(feedbackRequest.findAppraiserByEmail("john.doe@example.com").isPresent());

        // Try to add duplicate (with different case)
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> feedbackRequest.addAppraiser("JOHN.DOE@EXAMPLE.COM")
        );

        assertTrue(exception.getMessage().contains("already exists"));
    }
}