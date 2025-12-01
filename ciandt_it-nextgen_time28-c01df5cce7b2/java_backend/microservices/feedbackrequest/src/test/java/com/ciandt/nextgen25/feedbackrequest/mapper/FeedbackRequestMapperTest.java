package com.ciandt.nextgen25.feedbackrequest.mapper;

import com.ciandt.nextgen25.feedbackrequest.dtos.AnswerDTO;
import com.ciandt.nextgen25.feedbackrequest.dtos.AppraiserDTO;
import com.ciandt.nextgen25.feedbackrequest.dtos.FeedbackDTO;
import com.ciandt.nextgen25.feedbackrequest.dtos.FeedbackRequestCiandTDTO;
import com.ciandt.nextgen25.feedbackrequest.dtos.QuestionDTO;
import com.ciandt.nextgen25.feedbackrequest.entity.Answer;
import com.ciandt.nextgen25.feedbackrequest.entity.Appraiser;
import com.ciandt.nextgen25.feedbackrequest.entity.FeedbackRequest;
import com.ciandt.nextgen25.feedbackrequest.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackRequestMapperTest {

    @Mock
    private AppraiserMapper appraiserMapper;

    @Mock
    private QuestionMapper questionMapper;

    @Mock
    private AnswerMapper answerMapper;

    @InjectMocks
    private FeedbackRequestMapper feedbackRequestMapper;

    private FeedbackRequest feedbackRequest;
    private FeedbackRequestCiandTDTO feedbackRequestDTO;
    private Appraiser appraiser;
    private Question question1;
    private Question question2;
    private Question question3;
    private Answer answer;
    private AppraiserDTO appraiserDTO;
    private QuestionDTO questionDTO;
    private AnswerDTO answerDTO;
    private UUID id;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        now = LocalDateTime.now();

        // Setup entities
        feedbackRequest = new FeedbackRequest(1L);
        feedbackRequest.setId(id);
        feedbackRequest.setCreatedAt(now);

        appraiser = new Appraiser("test@example.com");
        question1 = new Question("Test question?");
        question2 = new Question("Test question 2?");
        question3 = new Question("Test question 3?");
        answer = new Answer(appraiser.getEmail(), question1.getText());

        feedbackRequest.addAppraiser(appraiser.getEmail());
        feedbackRequest.addQuestion(question1.getText());
        feedbackRequest.addQuestion(question2.getText());
        feedbackRequest.addQuestion(question3.getText());
        feedbackRequest.getAnswers().add(answer);

        // Setup DTOs
        feedbackRequestDTO = new FeedbackRequestCiandTDTO();
        feedbackRequestDTO.setId(id);
        feedbackRequestDTO.setRequesterId(1L);
        feedbackRequestDTO.setCreatedAt(LocalDateTime.now());

        appraiserDTO = new AppraiserDTO();
        appraiserDTO.setEmail("test@example.com");

        questionDTO = new QuestionDTO();
        questionDTO.setText("Test question?");

        answerDTO = new AnswerDTO();
        answerDTO.setText("Test answer");

        feedbackRequestDTO.setAppraisers(Collections.singletonList(appraiserDTO));
        feedbackRequestDTO.setQuestions(Collections.singletonList(questionDTO));
        feedbackRequestDTO.setAnswers(Collections.singletonList(answerDTO));

        lenient().when(appraiserMapper.toDTO(any())).thenReturn(appraiserDTO);
        lenient().when(questionMapper.toDTOList(any())).thenReturn(Collections.singletonList(questionDTO));
        lenient().when(answerMapper.toDTOList(any())).thenReturn(Collections.singletonList(answerDTO));
    }

    @Test
    void toDTO_ShouldReturnNull_WhenFeedbackRequestIsNull() {
        assertNull(feedbackRequestMapper.toDTO(null));
    }

    @Test
    void toDTO_ShouldMapAllFields() {
        // Set status fields

        feedbackRequest.approve();

        FeedbackRequestCiandTDTO result = feedbackRequestMapper.toDTO(feedbackRequest);

        // Verify all fields are mapped correctly
        assertEquals(id, result.getId());
        assertEquals(1L, result.getRequesterId());
        assertEquals(now, result.getCreatedAt());
        assertEquals(feedbackRequest.getApprovedAt(), result.getApprovedAt());

        // Verify child objects are mapped
        verify(appraiserMapper).toDTO(any(Appraiser.class));
        verify(questionMapper, times(3)).toDTO(any(Question.class));
        verify(answerMapper).toDTO(any(Answer.class));

        assertEquals(1, result.getAppraisers().size());
        assertEquals(3, result.getQuestions().size());
        assertEquals(1, result.getAnswers().size());

        // Verify status flags
        assertTrue(result.isApproved());
        assertFalse(result.isRejected());
        assertFalse(result.isExpired());
    }

    @Test
    void toFeedbackDTO_ShouldReturnNull_WhenFeedbackRequestIsNull() {
        assertNull(feedbackRequestMapper.toFeedbackDTO(null));
    }

    @Test
    void toFeedbackDTO_ShouldMapEssentialFields() {
        FeedbackDTO result = feedbackRequestMapper.toFeedbackDTO(feedbackRequest);

        assertEquals(id, result.id());
        assertEquals(1L, result.requesterId());
        assertEquals(now, result.createdAt());
        assertEquals("Pending", result.status());
    }

    @Test
    void toFeedbackDTO_ShouldSetCorrectStatus() {
        // Test different statuses

        // Approved
        feedbackRequest.approve();
        assertEquals("Approved", feedbackRequestMapper.toFeedbackDTO(feedbackRequest).status());

        // Rejected
        feedbackRequest = new FeedbackRequest(1L);
        feedbackRequest.setRejectedAt(LocalDateTime.now());
        assertEquals("Rejected", feedbackRequestMapper.toFeedbackDTO(feedbackRequest).status());

        // Expired
        feedbackRequest = new FeedbackRequest(1L);
        feedbackRequest.setCreatedAt(LocalDateTime.now().minusMonths(6));
        assertEquals("Expired", feedbackRequestMapper.toFeedbackDTO(feedbackRequest).status());

        // Edited
        feedbackRequest = new FeedbackRequest(1L);
        feedbackRequest.markAsEdited();
        assertEquals("Edited", feedbackRequestMapper.toFeedbackDTO(feedbackRequest).status());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDTOIsNull() {
        assertNull(feedbackRequestMapper.toEntity(null));
    }

    @Test
    void toEntity_ShouldCreateNewEntity() {
        // Setup DTO with required data
        feedbackRequestDTO.setRequesterId(999L);
        feedbackRequestDTO.setCreatedAt(now);
        feedbackRequestDTO.setApprovedAt(now.plusDays(1));
        feedbackRequestDTO.setQuestions(Collections.singletonList(questionDTO));
        feedbackRequestDTO.setAppraisers(Collections.singletonList(appraiserDTO));

        FeedbackRequest result = feedbackRequestMapper.toEntity(feedbackRequestDTO);

        // Verify entity is created with correct data
        assertEquals(999L, result.getRequesterId());
        assertEquals(now, result.getCreatedAt());
        assertEquals(now.plusDays(1), result.getApprovedAt());
        assertEquals(1, result.getQuestions().size());
        assertEquals(1, result.getAppraisers().size());
        assertEquals(0, result.getAnswers().size());  // No answers should be added
    }

    @Test
    void updateEntityFromDTO_ShouldDoNothing_WhenEitherParameterIsNull() {
        feedbackRequestMapper.updateEntityFromDTO(null, feedbackRequestDTO);
        feedbackRequestMapper.updateEntityFromDTO(feedbackRequest, null);

        // No exceptions should be thrown
    }

    @Test
    void updateEntityFromDTO_ShouldUpdateEntity() {
        // Setup original entity
        FeedbackRequest originalEntity = new FeedbackRequest(1L);
        originalEntity.addAppraiser("original@example.com");
        originalEntity.addQuestion("Original question?");

        // Setup updated DTO
        FeedbackRequestCiandTDTO updatedDTO = new FeedbackRequestCiandTDTO();
        updatedDTO.setRequesterId(2L);

        AppraiserDTO newAppraiserDTO = new AppraiserDTO();
        newAppraiserDTO.setEmail("new@example.com");
        updatedDTO.setAppraisers(Collections.singletonList(newAppraiserDTO));

        QuestionDTO newQuestionDTO = new QuestionDTO();
        newQuestionDTO.setText("New question?");
        updatedDTO.setQuestions(Collections.singletonList(newQuestionDTO));

        // Perform update
        feedbackRequestMapper.updateEntityFromDTO(originalEntity, updatedDTO);

        // Verify entity was updated
        assertEquals(2L, originalEntity.getRequesterId());
        assertEquals(1, originalEntity.getAppraisers().size());
        assertEquals("new@example.com", originalEntity.getAppraisers().get(0).getEmail());
        assertEquals(1, originalEntity.getQuestions().size());
        assertEquals("New question?", originalEntity.getQuestions().get(0).getText());
        assertTrue(originalEntity.isEdited());
    }

    @Test
    void toFeedbackDTOList_ShouldReturnEmptyList_WhenInputIsNull() {
        List<FeedbackDTO> result = feedbackRequestMapper.toFeedbackDTOList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toFeedbackDTOList_ShouldMapAllEntities() {
        // Setup multiple entities
        FeedbackRequest request1 = new FeedbackRequest(1L);
        request1.setId(UUID.randomUUID());

        FeedbackRequest request2 = new FeedbackRequest(2L);
        request2.setId(UUID.randomUUID());

        List<FeedbackRequest> requests = Arrays.asList(request1, request2);

        // Test conversion
        List<FeedbackDTO> result = feedbackRequestMapper.toFeedbackDTOList(requests);

        assertEquals(2, result.size());
        assertEquals(request1.getId(), result.get(0).id());
        assertEquals(request2.getId(), result.get(1).id());
    }

    @Test
    void toDTOList_ShouldReturnEmptyList_WhenInputIsNull() {
        List<FeedbackRequestCiandTDTO> result = feedbackRequestMapper.toDTOList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOList_ShouldMapAllEntities() {
        // Setup multiple entities
        FeedbackRequest request1 = new FeedbackRequest(1L);
        request1.setId(UUID.randomUUID());

        FeedbackRequest request2 = new FeedbackRequest(2L);
        request2.setId(UUID.randomUUID());

        List<FeedbackRequest> requests = Arrays.asList(request1, request2);

        // Test conversion
        List<FeedbackRequestCiandTDTO> result = feedbackRequestMapper.toDTOList(requests);

        assertEquals(2, result.size());
        assertEquals(request1.getId(), result.get(0).getId());
        assertEquals(request2.getId(), result.get(1).getId());
    }
}