package com.ciandt.nextgen25.feedbackrequest.service;

import com.ciandt.nextgen25.feedbackrequest.entity.*;
import com.ciandt.nextgen25.feedbackrequest.repository.FeedbackRequestRepository;
import com.ciandt.nextgen25.feedbackrequest.dtos.*;
import com.ciandt.nextgen25.feedbackrequest.exceptions.*;
import com.ciandt.nextgen25.security.entity.LoggedUser;
import com.ciandt.nextgen25.security.service.JwtTokenService;

import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.*;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.*;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class FeedbackRequestServiceTest {

    @Mock
    private FeedbackRequestRepository repository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private FeedbackRequestService service;

    @Mock
    private JwtTokenService jwtTokenService;

    private static final String AUTH_TOKEN = "dummyToken";
    private FeedbackRequest feedbackRequest;

    @Mock
    private RestTemplate restTemplate;

    @Value("${usermanagement.service.url}")
    private String userManagementServiceUrl;

    @BeforeEach
    public void setUp() {
        feedbackRequest = new FeedbackRequest();
        feedbackRequest.setRequesterId(1L);
        ReflectionTestUtils.setField(service, "userManagementServiceUrl", "http://test-url");
    }

    @Test
    void testListFeedbacks() {
        // Arrange
        FeedbackRequest finishedRequest = mock(FeedbackRequest.class);
        FeedbackRequest toApproveRequest = mock(FeedbackRequest.class);
        FeedbackRequest waitingAnswerRequest = mock(FeedbackRequest.class);
        FeedbackRequest expiredRequest = mock(FeedbackRequest.class);
        FeedbackRequest toCreateRequest = mock(FeedbackRequest.class);
        FeedbackRequest rejectRequest = mock(FeedbackRequest.class);

        configureMockFeedbackRequest(finishedRequest, "Finalizado");
        configureMockFeedbackRequest(toApproveRequest, "Em aprovação");
        configureMockFeedbackRequest(waitingAnswerRequest, "Aguardando respostas");
        configureMockFeedbackRequest(expiredRequest, "Expirado");
        configureMockFeedbackRequest(toCreateRequest, "Em criação");
        configureMockFeedbackRequest(rejectRequest, "Rejeitado");

        List<FeedbackRequest> mockRequests = Arrays.asList(
                finishedRequest, toApproveRequest, waitingAnswerRequest,
                expiredRequest, toCreateRequest, rejectRequest
        );

        when(repository.findAll()).thenReturn(mockRequests);

        // Act
        List<FeedbackDTO> result = service.listFeedbacks();

        // Assert
        assertEquals(6, result.size());

        assertStatus(result.get(0), "Finalizado", finishedRequest);
        assertStatus(result.get(1), "Em aprovação", toApproveRequest);
        assertStatus(result.get(2), "Aguardando respostas", waitingAnswerRequest);
        assertStatus(result.get(3), "Expirado", expiredRequest);
        assertStatus(result.get(4), "Em criação", toCreateRequest);
        assertStatus(result.get(5), "Rejeitado", rejectRequest);

        verify(repository).findAll();
    }

    private void configureMockFeedbackRequest(FeedbackRequest request, String status) {
        when(request.getId()).thenReturn(UUID.randomUUID());
        when(request.getRequesterId()).thenReturn(1L);
        when(request.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(request.getRequestStatus()).thenReturn(status);
    }

    private void assertStatus(FeedbackDTO dto, String expectedStatus, FeedbackRequest request) {
        assertEquals(expectedStatus, dto.status());
        assertEquals(request.getId(), dto.id());
        assertEquals(request.getRequesterId(), dto.requesterId());
        assertEquals(request.getCreatedAt(), dto.createdAt());
    }

    @Test
    void testFindById_Success() {
        // Arrange
        UUID id = UUID.randomUUID();
        FeedbackRequest expectedRequest = mock(FeedbackRequest.class);
        when(expectedRequest.getId()).thenReturn(id);
        when(repository.findById(id)).thenReturn(Optional.of(expectedRequest));

        // Act
        FeedbackRequest result = service.findById(id);

        // Assert
        assertEquals(id, result.getId());
        verify(repository).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(id)
        );

        assertEquals("Feedback request not found with id: " + id, exception.getMessage());
        verify(repository).findById(id);
    }

    @Test
    void testGetFeedbackFormById_Success() {
        // Arrange
        UUID id = UUID.randomUUID();
        FeedbackRequest request = mock(FeedbackRequest.class);
        when(request.getId()).thenReturn(id);
        when(request.getRequesterId()).thenReturn(1L);
        LocalDateTime createdAt = LocalDateTime.now();
        when(request.getCreatedAt()).thenReturn(createdAt);

        // Setup questions
        List<Question> questions = new ArrayList<>();
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        when(q1.getText()).thenReturn("Question 1");
        when(q2.getText()).thenReturn("Question 2");
        questions.add(q1);
        questions.add(q2);

        // Setup appraisers
        List<Appraiser> appraisers = new ArrayList<>();
        Appraiser a1 = mock(Appraiser.class);
        Appraiser a2 = mock(Appraiser.class);
        when(a1.getEmail()).thenReturn("appraiser1@example.com");
        when(a2.getEmail()).thenReturn("appraiser2@example.com");
        appraisers.add(a1);
        appraisers.add(a2);

        // Setup appraiser status
        Map<String, String> appraiserStatus = new HashMap<>();
        appraiserStatus.put("appraiser1@example.com", "Não Respondido");
        appraiserStatus.put("appraiser2@example.com", "Respondido");

        when(request.getQuestions()).thenReturn(questions);
        when(request.getAppraisers()).thenReturn(appraisers);
        when(request.getAppraisersWithStatus()).thenReturn(appraiserStatus);
        when(repository.findById(id)).thenReturn(Optional.of(request));

        // Act
        FeedbackFormDTO result = service.getFeedbackFormById(id);

        // Assert
        assertEquals(id, result.id());
        assertEquals(1L, result.requesterId());
        assertEquals(createdAt, result.createdAt());
        assertEquals(2, result.questions().size());
        assertEquals("Question 1", result.questions().get(0));
        assertEquals("Question 2", result.questions().get(1));
        assertEquals(2, result.appraisers().size());
        assertEquals("appraiser1@example.com", result.appraisers().get(0));
        assertEquals("appraiser2@example.com", result.appraisers().get(1));
        assertEquals("Não Respondido", result.appraiserStatus().get("appraiser1@example.com"));
        assertEquals("Respondido", result.appraiserStatus().get("appraiser2@example.com"));

        verify(repository).findById(id);
    }

    @Test
    void testGetFeedbackFormById_HandlesNullElements() {
        // Arrange
        UUID id = UUID.randomUUID();
        FeedbackRequest request = mock(FeedbackRequest.class);
        when(request.getId()).thenReturn(id);
        when(request.getRequesterId()).thenReturn(1L);
        when(request.getCreatedAt()).thenReturn(LocalDateTime.now());

        // Setup questions with null elements
        List<Question> questions = new ArrayList<>();
        Question q1 = mock(Question.class);
        when(q1.getText()).thenReturn("Valid Question");
        questions.add(q1);
        questions.add(null);

        Question q3 = mock(Question.class);
        when(q3.getText()).thenReturn(null);
        questions.add(q3);

        // Setup appraisers with null elements
        List<Appraiser> appraisers = new ArrayList<>();
        Appraiser a1 = mock(Appraiser.class);
        when(a1.getEmail()).thenReturn("valid@example.com");
        appraisers.add(a1);
        appraisers.add(null);

        Appraiser a3 = mock(Appraiser.class);
        when(a3.getEmail()).thenReturn(null);
        appraisers.add(a3);

        // Setup appraiser status
        Map<String, String> appraiserStatus = new HashMap<>();
        appraiserStatus.put("valid@example.com", "Não Respondido");

        when(request.getQuestions()).thenReturn(questions);
        when(request.getAppraisers()).thenReturn(appraisers);
        when(request.getAppraisersWithStatus()).thenReturn(appraiserStatus);
        when(repository.findById(id)).thenReturn(Optional.of(request));

        // Act
        FeedbackFormDTO result = service.getFeedbackFormById(id);

        // Assert
        assertEquals(id, result.id());
        assertEquals(1, result.questions().size());
        assertEquals("Valid Question", result.questions().get(0));
        assertEquals(1, result.appraisers().size());
        assertEquals("valid@example.com", result.appraisers().get(0));
        assertEquals("Não Respondido", result.appraiserStatus().get("valid@example.com"));

        verify(repository).findById(id);
    }

    @Test
    void testGetFeedbackFormById_HandlesNullLists() {
        // Arrange
        UUID id = UUID.randomUUID();
        FeedbackRequest request = mock(FeedbackRequest.class);
        when(request.getId()).thenReturn(id);
        when(request.getRequesterId()).thenReturn(1L);
        when(request.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(request.getQuestions()).thenReturn(null);
        when(request.getAppraisers()).thenReturn(null);
        when(request.getAppraisersWithStatus()).thenReturn(Collections.emptyMap());

        when(repository.findById(id)).thenReturn(Optional.of(request));

        // Act
        FeedbackFormDTO result = service.getFeedbackFormById(id);

        // Assert
        assertEquals(id, result.id());
        assertTrue(result.questions().isEmpty());
        assertTrue(result.appraisers().isEmpty());
        assertTrue(result.appraiserStatus().isEmpty());

        verify(repository).findById(id);
    }

    @Test
    void testCreateFeedbackRequest() {
        // Arrange
        Long requesterId = 1L;
        List<String> questions = Arrays.asList("Question 1", "Question 2");
        List<String> appraiserEmails = Arrays.asList("appraiser1@example.com", "appraiser2@example.com");

        FeedbackRequest mockRequest = mock(FeedbackRequest.class);
        when(repository.save(any(FeedbackRequest.class))).thenReturn(mockRequest);

        // Act
        FeedbackRequest result = service.createFeedbackRequest(requesterId, questions, appraiserEmails);

        // Assert
        assertEquals(mockRequest, result);
        verify(repository).save(any(FeedbackRequest.class));
    }

    @Test
    void testSendEmailsForFeedbackRequest() {
        // Arrange
        FeedbackRequest mockRequest = mock(FeedbackRequest.class);
        Appraiser appraiser1 = mock(Appraiser.class);
        Appraiser appraiser2 = mock(Appraiser.class);

        when(appraiser1.getEmail()).thenReturn("appraiser1@example.com");
        when(appraiser2.getEmail()).thenReturn("appraiser2@example.com");
        when(mockRequest.getAppraisers()).thenReturn(Arrays.asList(appraiser1, appraiser2));

        // Act
        service.sendEmailsForFeedbackRequest(mockRequest);

        // Assert
        verify(emailService, times(2)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testReviewFeedbackRequest_WhenApproved() {
        // Arrange
        UUID id = UUID.randomUUID();
        FeedbackRequest mockRequest = mock(FeedbackRequest.class);

        when(repository.findById(id)).thenReturn(Optional.of(mockRequest));
        when(repository.save(mockRequest)).thenReturn(mockRequest);

        // Act
        FeedbackRequest result = service.reviewFeedbackRequest(id, true, null);

        // Assert
        verify(mockRequest).approve();
        verify(repository).save(mockRequest);
        assertEquals(mockRequest, result);
    }

    @Test
    void testReviewFeedbackRequest_WhenRejected() {
        // Arrange
        UUID id = UUID.randomUUID();
        String rejectMessage = "Rejected for testing";
        FeedbackRequest mockRequest = mock(FeedbackRequest.class);

        when(repository.findById(id)).thenReturn(Optional.of(mockRequest));
        when(repository.save(mockRequest)).thenReturn(mockRequest);

        // Act
        FeedbackRequest result = service.reviewFeedbackRequest(id, false, rejectMessage);

        // Assert
        verify(mockRequest).reject(rejectMessage);
        verify(repository).save(mockRequest);
        assertEquals(mockRequest, result);
    }

    @Test
    void shouldMapFeedbackRequestToDTO_WithAllFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        Long requesterId = 123L;
        LocalDateTime now = LocalDateTime.now();

        Appraiser appraiser1 = new Appraiser();
        appraiser1.setEmail("john.doe@example.com");

        Appraiser appraiser2 = new Appraiser();
        appraiser2.setEmail("jane.smith@example.com");

        // Criando perguntas
        Question question1 = new Question();
        question1.setText("Como foi o desempenho?");

        Question question2 = new Question();
        question2.setText("Quais áreas precisam melhorar?");

        Question question3 = new Question();
        question3.setText("Qual nota daria para o meu desempenho?");

        // Criando respostas
        Answer answer1 = new Answer();
        answer1.setAppraiserEmail("john.doe@example.com");
        answer1.setText("Desempenho excelente");

        Answer answer2 = new Answer();
        answer2.setAppraiserEmail("john.doe@example.com");
        answer2.setText("Precisa melhorar pontualidade");

        Answer answer3 = new Answer();
        answer3.setAppraiserEmail("jane.smith@example.com");
        answer3.setText("Desempenho regular");

        Answer answer4 = new Answer();
        answer4.setAppraiserEmail("jane.smith@example.com");
        answer4.setText("Precisa melhorar comunicação");

        // Criando o objeto FeedbackRequest
        FeedbackRequest feedbackRequest = new FeedbackRequest();
        feedbackRequest.setId(id);
        feedbackRequest.setRequesterId(requesterId);
        feedbackRequest.setCreatedAt(now);
        feedbackRequest.setApprovedAt(now.plusDays(1));
        feedbackRequest.setRejectedAt(null);
        feedbackRequest.setEditedAt(now.plusDays(2));
        feedbackRequest.addAppraiser(appraiser1.getEmail());
        feedbackRequest.addAppraiser(appraiser2.getEmail());
        feedbackRequest.addQuestion(question1.getText());
        feedbackRequest.addQuestion(question2.getText());
        feedbackRequest.addQuestion(question3.getText());
        feedbackRequest.submitAnswer(appraiser1.getEmail(), answer1.getText());
        feedbackRequest.submitAnswer(appraiser1.getEmail(), answer2.getText());
        feedbackRequest.submitAnswer(appraiser2.getEmail(), answer3.getText());
        feedbackRequest.submitAnswer(appraiser2.getEmail(), answer4.getText());
        feedbackRequest.setApprovedAt(now);
        feedbackRequest.isRejected();
        feedbackRequest.isExpired();

        // Mock do repositório
        when(repository.findByRequesterId(requesterId)).thenReturn(List.of(feedbackRequest));

        // Act - chamando o metodo público que usa o mapToDTO internamente
        List<FeedbackRequestCiandTDTO> results = service.getFeedbackRequestByRequesterId(requesterId);

        // Act
        FeedbackRequestCiandTDTO result = service.mapToDTO(feedbackRequest);

        // Assert
        assertEquals(id, result.getId());
        assertEquals(requesterId, result.getRequesterId());
        assertEquals(now, result.getCreatedAt());
        assertEquals(now, result.getApprovedAt());
        assertNull(result.getRejectedAt());
        assertEquals(now.plusDays(2), result.getEditedAt());

        // Verificar avaliadores
        assertEquals(2, result.getAppraisers().size());
        assertEquals("john.doe@example.com", result.getAppraisers().get(0).getEmail());
        assertEquals("jane.smith@example.com", result.getAppraisers().get(1).getEmail());

        // Verificar perguntas
        assertEquals(3, result.getQuestions().size());
        assertEquals("Como foi o desempenho?", result.getQuestions().get(0).getText());
        assertEquals("Quais áreas precisam melhorar?", result.getQuestions().get(1).getText());

        // Verificar respostas
        assertEquals(4, result.getAnswers().size());
        assertEquals("john.doe@example.com", result.getAnswers().get(0).getAppraiserEmail());
        assertEquals("Desempenho excelente", result.getAnswers().get(0).getText());
        assertEquals("jane.smith@example.com", result.getAnswers().get(2).getAppraiserEmail());
        assertEquals("Desempenho regular", result.getAnswers().get(2).getText());

        // Verificar estados
        assertTrue(result.isApproved());
        assertFalse(result.isRejected());
        assertFalse(result.isExpired());
    }

    @Test
    public void notifyPDMtoApproveFeedbackRequest_UserNotFound() {
        when(restTemplate.exchange(anyString(), any(), any(), eq(UserDTO.class)))
                .thenReturn(ResponseEntity.notFound().build()); // Exemplo de resposta 404

        service.notifyPDMtoApproveFeedbackRequest(feedbackRequest, AUTH_TOKEN);

        verify(emailService, times(0)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void notifyPDMtoApproveFeedbackRequest_PDMNotFound() {
        UserDTO user = new UserDTO();
        user.setPdmId(null);

        when(restTemplate.exchange(anyString(), any(), any(), eq(UserDTO.class)))
                .thenReturn(ResponseEntity.ok(user));

        service.notifyPDMtoApproveFeedbackRequest(feedbackRequest, AUTH_TOKEN);

        verify(emailService, times(0)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void getCollaboratorsByPdm_EmptyResponse() throws JsonProcessingException {
        // Arrange
        String authToken = "validToken";
        String serviceUrl = "http://user-management-service";
        ReflectionTestUtils.setField(service, "userManagementServiceUrl", serviceUrl);
        LoggedUser mockUser = mock(LoggedUser.class);
        when(mockUser.getId()).thenReturn(123L);
        when(jwtTokenService.getUserFromToken(authToken)).thenReturn(mockUser);
        // Mock RestTemplate exchange method returning null body
        ResponseEntity<List<UserInfoDTO>> mockResponse = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(serviceUrl + "/pdm/123/collaborators"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponse);
        // Act
        List<UserInfoDTO> result = service.getCollaboratorsByPdm(authToken);
        // Assert
        assertNull(result);  // Since we're not adding the null check in the service method
    }

    @Test
    void getCollaboratorsByPdm_HttpClientError() throws JsonProcessingException {
        // Arrange
        String authToken = "validToken";
        String serviceUrl = "http://user-management-service";
        ReflectionTestUtils.setField(service, "userManagementServiceUrl", serviceUrl);
        LoggedUser mockUser = mock(LoggedUser.class);
        when(mockUser.getId()).thenReturn(123L);
        when(jwtTokenService.getUserFromToken(authToken)).thenReturn(mockUser);
        // Mock RestTemplate throwing HttpClientErrorException
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");
        when(restTemplate.exchange(
                eq(serviceUrl + "/pdm/123/collaborators"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenThrow(exception);
        // Act & Assert
        HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class,
                () -> service.getCollaboratorsByPdm(authToken));
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
    }

    @Test
    void getCollaboratorsByPdm_HttpServerError() throws JsonProcessingException {
        // Arrange
        String authToken = "validToken";
        String serviceUrl = "http://user-management-service";
        ReflectionTestUtils.setField(service, "userManagementServiceUrl", serviceUrl);
        LoggedUser mockUser = mock(LoggedUser.class);
        when(mockUser.getId()).thenReturn(123L);
        when(jwtTokenService.getUserFromToken(authToken)).thenReturn(mockUser);
        // Mock RestTemplate throwing HttpServerErrorException
        HttpServerErrorException exception = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error");
        when(restTemplate.exchange(
                eq(serviceUrl + "/pdm/123/collaborators"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenThrow(exception);
        // Act & Assert
        HttpServerErrorException thrown = assertThrows(HttpServerErrorException.class,
                () -> service.getCollaboratorsByPdm(authToken));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatusCode());
    }

    @Test
    void getFeedbackRequestsByUsers_WithValidCollaborators_ShouldReturnCorrectDTOs() {
        // Arrange
        UserInfoDTO user1 = new UserInfoDTO(101L, "john@example.com", "DEVELOPER", "SENIOR");
        UserInfoDTO user2 = new UserInfoDTO(102L, "jane@example.com", "DEVELOPER", "JUNIOR");
        List<UserInfoDTO> collaborators = Arrays.asList(user1, user2);
        // Mock FeedbackRequest for user1
        FeedbackRequest request1 = new FeedbackRequest();
        request1.setRequesterId(101L);
        request1.setCreatedAt(LocalDateTime.now().minusDays(5));
        // Mock FeedbackRequest for user2
        FeedbackRequest request2 = new FeedbackRequest();
        request2.setRequesterId(102L);
        request2.setCreatedAt(LocalDateTime.now().minusDays(2));
        // Mock repository behavior
        when(repository.findByRequesterId(101L)).thenReturn(Collections.singletonList(request1));
        when(repository.findByRequesterId(102L)).thenReturn(Collections.singletonList(request2));
        // Create spy to test private determineStatus method
        FeedbackRequestService serviceSpy = spy(service);
        FeedbackRequest feedbackRequestSpy = spy(feedbackRequest);
        // Act
        List<PDMFeedbackDTO> result = serviceSpy.getFeedbackRequestsByUsers(collaborators);
        // Assert
        assertEquals(2, result.size());
        // Check first result - should be more recent (request2)
        assertEquals(user2, result.get(0).getRequester());
        assertEquals("Em aprovação", result.get(0).getStatus());
        assertEquals(request2.getCreatedAt(), result.get(0).getCreatedAt());
        // Check second result
        assertEquals(user1, result.get(1).getRequester());
        assertEquals("Em aprovação", result.get(1).getStatus());
        assertEquals(request1.getCreatedAt(), result.get(1).getCreatedAt());
        // Verify repository calls
        verify(repository).findByRequesterId(101L);
        verify(repository).findByRequesterId(102L);
    }

    @Test
    void getFeedbackRequestsByUsers_WithEmptyCollaborators_ShouldReturnEmptyList() {
        // Act
        List<PDMFeedbackDTO> result = service.getFeedbackRequestsByUsers(Collections.emptyList());
        // Assert
        assertTrue(result.isEmpty());
        verify(repository, never()).findByRequesterId(anyLong());
    }

    @Test
    void getFeedbackRequestsByUsers_WithNullCollaborators_ShouldReturnEmptyList() {
        // Act
        List<PDMFeedbackDTO> result = service.getFeedbackRequestsByUsers(null);
        // Assert
        assertTrue(result.isEmpty());
        verify(repository, never()).findByRequesterId(anyLong());
    }

    @Test
    void getFeedbackRequestsByUsers_WithRepositoryException_ShouldContinueProcessing() {
        // Arrange
        UserInfoDTO user1 = new UserInfoDTO(101L, "john@example.com", "DEVELOPER", "SENIOR");
        UserInfoDTO user2 = new UserInfoDTO(102L, "jane@example.com", "DEVELOPER", "JUNIOR");
        List<UserInfoDTO> collaborators = Arrays.asList(user1, user2);
        // Mock repository to throw exception for first user
        when(repository.findByRequesterId(101L)).thenThrow(new RuntimeException("Database error"));
        // Mock successful repository call for second user
        FeedbackRequest request2 = new FeedbackRequest();
        request2.setRequesterId(102L);
        request2.setCreatedAt(LocalDateTime.now());
        when(repository.findByRequesterId(102L)).thenReturn(Collections.singletonList(request2));
        // Act
        List<PDMFeedbackDTO> result = service.getFeedbackRequestsByUsers(collaborators);
        // Assert
        assertEquals(1, result.size());
        assertEquals(user2, result.get(0).getRequester());
        assertEquals("Em aprovação", result.get(0).getStatus());
        // Verify repository calls
        verify(repository).findByRequesterId(101L);
        verify(repository).findByRequesterId(102L);
    }

    @Test
    void determineStatus_WhenRejected_ShouldReturnRejeitado() {
        // Arrange
        FeedbackRequest request = mock(FeedbackRequest.class);
        when(request.isRejected()).thenReturn(true);
        when(request.getRequestStatus()).thenCallRealMethod();

        // Act
        String result = request.getRequestStatus();

        // Assert
        assertEquals("Rejeitado", result);
    }

    @Test
    void determineStatus_WhenExpired_ShouldReturnExpirado() {
        // Arrange
        FeedbackRequest request = mock(FeedbackRequest.class);
        when(request.isRejected()).thenReturn(false);
        when(request.isExpired()).thenReturn(true);
        when(request.getRequestStatus()).thenCallRealMethod();

        // Act
        String result = request.getRequestStatus();

        // Assert
        assertEquals("Expirado", result);
    }

    @Test
    void hasAllAppraisersAnswered_WhenAllAppraisersHaveAnsweredAllQuestions_ShouldReturnTrue() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest();
        Appraiser appraiser1 = new Appraiser("john@example.com");
        Appraiser appraiser2 = new Appraiser("jane@example.com");
        request.getAppraisers().addAll(Arrays.asList(appraiser1, appraiser2));

        Question question1 = new Question("Question 1");
        Question question2 = new Question("Question 2");
        request.getQuestions().addAll(Arrays.asList(question1, question2));

        Answer answer1 = new Answer("john@example.com", "Answer 1");
        Answer answer2 = new Answer("john@example.com", "Answer 2");
        Answer answer3 = new Answer("jane@example.com", "Answer 1");
        Answer answer4 = new Answer("jane@example.com", "Answer 2");
        request.getAnswers().addAll(Arrays.asList(answer1, answer2, answer3, answer4));

        // Act
        boolean result = request.hasAllAppraisersAnswered();

        // Assert
        assertTrue(result);
    }

    @Test
    void determineStatus_WhenApprovedWithNoAnswers_ShouldReturnAguardandoRespostas() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest(1L);
        request.setApprovedAt(LocalDateTime.now());

        // Act
        String result = request.getRequestStatus();

        // Assert
        assertEquals("Aguardando respostas", result);
    }

    @Test
    void determineStatus_WhenCreatedWithDate_ShouldReturnEmAprovacao() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest(1L);

        // Act
        String result = request.getRequestStatus();

        // Assert
        assertEquals("Em aprovação", result);
    }

    @Test
    void determineStatus_WhenNotCreated_ShouldReturnEmCriacao() {
        // Arrange
        FeedbackRequest request = new FeedbackRequest();
        request.setCreatedAt(null);

        // Act
        String result = request.getRequestStatus();

        // Assert
        assertEquals("Em criação", result);
    }

    @Test
    void getCollaboratorsByPdm_InvalidToken() throws JsonProcessingException {
        // Arrange
        String authToken = "invalidToken";
        // No need for serviceUrl here as we're not reaching that part of the code
        when(jwtTokenService.getUserFromToken(authToken)).thenThrow(new ValidationException("Invalid token"));
        // Act & Assert
        assertThrows(ValidationException.class, () -> service.getCollaboratorsByPdm(authToken));
        verify(restTemplate, never()).exchange(
                anyString(),
                any(),
                any(),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    public void notifyPDMtoApproveFeedbackRequest_Success() {
        FeedbackRequest feedbackRequest = new FeedbackRequest();
        feedbackRequest.setRequesterId(1L);
        feedbackRequest.setId(UUID.randomUUID());

        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("Requester");
        user.setPdmId(2L);
        user.setPdmName("PDM");
        user.setPdmEmail("pdm@example.com");

        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                ArgumentMatchers.<Class<UserDTO>>any()
        )).thenReturn(ResponseEntity.ok(user));

        service.notifyPDMtoApproveFeedbackRequest(feedbackRequest, AUTH_TOKEN);

        String expectedUrl = "http://test-url/user/1";;
        verify(restTemplate).exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(),
                eq(UserDTO.class)
        );

        verify(emailService).sendEmail(
                eq("pdm@example.com"),
                eq("Solicitação de aprovação de Feedback - Solicitação #" + feedbackRequest.getId()),
                argThat(body ->
                        body.contains("Olá PDM") &&
                                body.contains("O colaborador Requester solicitou feedback") &&
                                body.contains("Atenciosamente")
                )
        );
    }

    @Test
    void getCollaboratorsByPdm_Success() throws JsonProcessingException {
        // Arrange
        String authToken = "validToken";
        String serviceUrl = "http://user-management-service";
        // Mock the field value for userManagementServiceUrl using ReflectionTestUtils
        ReflectionTestUtils.setField(service, "userManagementServiceUrl", serviceUrl);
        LoggedUser mockUser = mock(LoggedUser.class);
        when(mockUser.getId()).thenReturn(123L);
        when(jwtTokenService.getUserFromToken(authToken)).thenReturn(mockUser);
        List<UserInfoDTO> expectedUsers = Arrays.asList(
                new UserInfoDTO(1L , "john@example.com", "DEVELOPER", "JUNIOR"),
                new UserInfoDTO(2L, "jane@example.com", "DEVELOPER", "INTERN")
        );
        // Mock RestTemplate exchange method
        ResponseEntity<List<UserInfoDTO>> mockResponse = new ResponseEntity<>(expectedUsers, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(serviceUrl + "/pdm/123/collaborators"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponse);
        // Act
        List<UserInfoDTO> result = service.getCollaboratorsByPdm(authToken);
        // Assert
        assertEquals(2, result.size());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals("jane@example.com", result.get(1).getEmail());
        // Verify interactions
        verify(jwtTokenService).getUserFromToken(authToken);
        verify(restTemplate).exchange(
                eq(serviceUrl + "/pdm/123/collaborators"),
                eq(HttpMethod.GET),
                argThat(entity -> entity.getHeaders().getFirst("Authorization").equals("Bearer " + authToken)),
                any(ParameterizedTypeReference.class)
        );
    }
}