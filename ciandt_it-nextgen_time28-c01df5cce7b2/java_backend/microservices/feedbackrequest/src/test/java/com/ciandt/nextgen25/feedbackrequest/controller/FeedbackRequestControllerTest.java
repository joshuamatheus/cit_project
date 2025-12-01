package com.ciandt.nextgen25.feedbackrequest.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciandt.nextgen25.feedbackrequest.service.FeedbackRequestService;

import org.junit.jupiter.api.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.*;

import com.ciandt.nextgen25.feedbackrequest.dtos.*;
import com.ciandt.nextgen25.feedbackrequest.entity.*;
import com.ciandt.nextgen25.feedbackrequest.exceptions.*;
import com.ciandt.nextgen25.security.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mockito.MockitoAnnotations;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.ciandt.nextgen25.feedbackrequest.service.EmailService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class FeedbackRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeedbackRequestController feedbackRequestController;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private FeedbackRequestService serviceMock;

    @Mock
    private UserInterface userInterface;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(feedbackRequestController, "service", serviceMock);
        LoggedUser loggedUser = mock(LoggedUser.class);
        when(loggedUser.getId()).thenReturn(1L);
        when(loggedUser.getName()).thenReturn("testUser");
        when(loggedUser.getEmail()).thenReturn("test@example.com");
        when(loggedUser.getType()).thenReturn("COLABORADOR");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        loggedUser,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("COLABORADOR"))
                )
        );
    }

    @Test
    @WithMockUser(username = "colaborador", roles = {"COLABORADOR"})
    void getFeedbackRequestsByRequesterIdTest() throws Exception {
        Long requesterId = 123L;

        FeedbackRequestCiandTDTO feedbackRequestDTO = new FeedbackRequestCiandTDTO();
        feedbackRequestDTO.setId(UUID.randomUUID());
        feedbackRequestDTO.setRequesterId(requesterId);
        feedbackRequestDTO.setCreatedAt(LocalDateTime.now());

        FeedbackRequestCiandTDTO feedbackRequestDTO2 = new FeedbackRequestCiandTDTO();
        feedbackRequestDTO2.setId(UUID.randomUUID());
        feedbackRequestDTO2.setRequesterId(requesterId);
        feedbackRequestDTO2.setCreatedAt(LocalDateTime.now());

        List<FeedbackRequestCiandTDTO> feedbacks = Arrays.asList(feedbackRequestDTO, feedbackRequestDTO2);

        when(serviceMock.getFeedbackRequestByRequesterId(requesterId)).thenReturn(feedbacks);

        ResponseEntity<List<FeedbackRequestCiandTDTO>> response = feedbackRequestController.getFeedbackRequestByRequesterId(requesterId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(requesterId, response.getBody().get(0).getRequesterId());
        assertEquals(requesterId, response.getBody().get(1).getRequesterId());
    }

    @Test
    void testCreateFeedbackRequest() throws Exception {
        FeedbackRequest mockFeedbackRequest = new FeedbackRequest(1L);
        mockFeedbackRequest.addQuestion("Question 1?");
        mockFeedbackRequest.addQuestion("Question 2?");
        mockFeedbackRequest.addQuestion("Question 3?");
        mockFeedbackRequest.addAppraiser("appraiser@example.com");

        CreateFeedbackRequestDTO requestDTO = new CreateFeedbackRequestDTO(
                List.of("Question 1?", "Question 2?", "Question 3?"),
                List.of("appraiser@example.com")
        );

        when(serviceMock.createFeedbackRequest(
                eq(1L),
                anyList(),
                anyList()
        )).thenReturn(mockFeedbackRequest);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requesterId").value(1L))
                .andExpect(jsonPath("$.questions", hasSize(3)))
                .andExpect(jsonPath("$.appraiserEmails", hasSize(1)))
                .andExpect(jsonPath("$.appraiserEmails[0]").value("appraiser@example.com"));
    }

    @Test
    void testCreateFeedbackRequest_EmptyQuestions() throws Exception {
        CreateFeedbackRequestDTO requestDTO = new CreateFeedbackRequestDTO(
                Collections.emptyList(),
                List.of("appraiser@example.com")
        );

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.questions").value("The question list cannot be empty"));
    }

    @Test
    void testCreateFeedbackRequest_EmptyAppraisers() throws Exception {
        CreateFeedbackRequestDTO requestDTO = new CreateFeedbackRequestDTO(
                List.of("Question 1?", "Question 2?", "Question 3?"),
                Collections.emptyList()
        );

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.appraiserEmails").value("The appraiser list cannot be empty"));
    }

    @Test
    void testCreateFeedbackRequest_InternalCIandTEmail() throws Exception {
        CreateFeedbackRequestDTO requestDTO = new CreateFeedbackRequestDTO(
                List.of("Question 1?", "Question 2?", "Question 3?"),
                List.of("employee@ciandt.com")
        );

        when(serviceMock.createFeedbackRequest(
                eq(1L),
                anyList(),
                eq(List.of("employee@ciandt.com"))
        )).thenThrow(new ValidationException("The email cannot be from an internal employee."));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The email cannot be from an internal employee."));
    }

    @Test
    void testCreateFeedbackRequest_DuplicateAppraiserEmail() throws Exception {
        CreateFeedbackRequestDTO requestDTO = new CreateFeedbackRequestDTO(
                List.of("Question 1?", "Question 2?", "Question 3?"),
                List.of("appraiser@example.com", "appraiser@example.com")
        );

        when(serviceMock.createFeedbackRequest(
                eq(1L),
                anyList(),
                eq(List.of("appraiser@example.com", "appraiser@example.com"))
        )).thenThrow(new DuplicateResourceException("Appraiser with email appraiser@example.com already exists"));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("already exists")));
    }

    @Test
    void testCreateFeedbackRequest_EmptyQuestionText() throws Exception {
        CreateFeedbackRequestDTO requestDTO = new CreateFeedbackRequestDTO(
                List.of("Question 1?", "", "Question 3?"),
                List.of("appraiser@example.com")
        );

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors['questions[1]']").value("Question text cannot be blank"));
    }

    @Test
    void testCreateFeedbackRequest_TooManyQuestions() throws Exception {
        CreateFeedbackRequestDTO requestDTO = new CreateFeedbackRequestDTO(
                List.of("Question 1?", "Question 2?", "Question 3?", "Question 4?"),
                List.of("appraiser@example.com")
        );

        when(serviceMock.createFeedbackRequest(
                eq(1L),
                eq(List.of("Question 1?", "Question 2?", "Question 3?", "Question 4?")),
                anyList()
        )).thenThrow(new ValidationException("Feedback request must have at most 3 questions"));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("at most 3 questions")));
    }

    @Test
    void testCreateFeedbackRequest_InvalidEmailFormat() throws Exception {
        CreateFeedbackRequestDTO requestDTO = new CreateFeedbackRequestDTO(
                List.of("Question 1?", "Question 2?", "Question 3?"),
                List.of("invalid-email")
        );

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors['appraiserEmails[0]']").value("Invalid email format"));
    }


    @Test
    @WithMockUser(username = "colaborador", roles = {"COLABORADOR"})
    @Disabled("Teste incompleto: implementação pendente")
    void deveCriarPedidoDeFeedback() throws Exception {
        String jsonPedido = "{\"perguntas\": [\"Pergunta 1\", \"Pergunta 2\"], \"criadorId\": \"1\"}";

        mockMvc.perform(post("/feedbackrequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPedido))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "colaborador", roles = {"COLABORADOR"})
    void deveListarPedidosDeFeedback() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        when(serviceMock.listFeedbacks()).thenReturn(
                Arrays.asList(
                        new FeedbackDTO(id1, 1L, now, "Finalizado"),
                        new FeedbackDTO(id2, 2L, now, "Aguardando respostas"),
                        new FeedbackDTO(id3, 3L, now, "Rejeitado")
                )
        );

        mockMvc.perform(get("/feedbacklist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(id1.toString()))
                .andExpect(jsonPath("$[0].requesterId").value(1))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].status").value("Finalizado"))
                .andExpect(jsonPath("$[1].id").value(id2.toString()))
                .andExpect(jsonPath("$[1].requesterId").value(2))
                .andExpect(jsonPath("$[1].createdAt").exists())
                .andExpect(jsonPath("$[1].status").value("Aguardando respostas"))
                .andExpect(jsonPath("$[2].id").value(id3.toString()))
                .andExpect(jsonPath("$[2].requesterId").value(3))
                .andExpect(jsonPath("$[2].createdAt").exists())
                .andExpect(jsonPath("$[2].status").value("Rejeitado"));

        verify(serviceMock, times(1)).listFeedbacks();
    }

    @Test
    @WithMockUser(username = "pdm", roles = {"PDM"})
    @Disabled("Teste incompleto: implementação pendente")
    void deveAprovarPedidoDeFeedback() throws Exception {
        String jsonAprovacao = "{\"pedidoId\": \"1\"}";

        mockMvc.perform(post("/feedbackrequests/1/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAprovacao))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "pdm", roles = {"PDM"})
    @Disabled("Teste incompleto: implementação pendente")
    void naoDeveAprovarSeuProprioPedidoDeFeedback() throws Exception {
        String jsonAprovacao = "{\"pedidoId\": \"1\"}";

        mockMvc.perform(post("/feedbackrequests/1/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAprovacao))
                .andExpect(status().isForbidden()); // Espera que a aprovação falhe
    }

    @Test
    @WithMockUser(username = "colaborador", roles = {"COLABORADOR"})
    @Disabled("Teste incompleto: implementação pendente")
    void deveEditarFeedbackSomenteQuandoRejeitado() throws Exception {
        String jsonEdicao = "{\"perguntas\": [\"Pergunta editada\"]}";

        // Tente editar um feedback que não foi rejeitado
        mockMvc.perform(put("/feedbacks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEdicao))
                .andExpect(status().isForbidden()); // Espera que a edição falhe
    }

    @Test
    @WithMockUser(username = "colaborador", roles = {"COLABORADOR"})
    @Disabled("Teste incompleto: implementação pendente")
    void deveObterFeedbackPorId() throws Exception {
        mockMvc.perform(get("/feedbacks/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "pdm", roles = {"PDM"})
    @Disabled("Teste incompleto: implementação pendente")
    void deveListarFeedbacksNaoAprovadosDosDiretos() throws Exception {
        mockMvc.perform(get("/pdms/1/directs/1/feedbackrequests")
                        .param("isApproved", "false"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "pdm", roles = {"PDM"})
    @Disabled("Teste incompleto: implementação pendente")
    void deveListarSomenteRequisicoesDosDiretos() throws Exception {
        mockMvc.perform(get("/pdms/1/directs/1/feedbackrequests")
                        .param("isApproved", "true"))
                .andExpect(status().isOk());

        // Tente listar requisições que não pertencem ao PDM
        mockMvc.perform(get("/pdms/2/directs/1/feedbackrequests")
                        .param("isApproved", "false"))
                .andExpect(status().isForbidden()); // Espera que a listagem falhe
    }

    @Test
    @WithMockUser(username = "avaliador", roles = {"AVALIADOR"})
    @Disabled("Teste incompleto: implementação pendente")
    void deveRejeitarAvaliadorComEmailInterno() throws Exception {
        // Simule um cenário onde um avaliador com email interno tenta ser adicionado
        String jsonAvaliador = "{\"email\": \"avaliador@ciandt.com.br\"}";

        mockMvc.perform(post("/avaliadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAvaliador))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarMensagemDeSucessoAoEnviarEmail() throws Exception {
        Appraiser appraiser = new Appraiser( "avaliador@example.com");
        FeedbackRequest feedbackRequest = new FeedbackRequest(1L);
        feedbackRequest.getAppraisers().add(appraiser);

        doNothing().when(serviceMock).sendEmailFromFeedback(eq(feedbackRequest), eq(appraiser));
        ResponseEntity<String> response = feedbackRequestController.askFeedback(feedbackRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email(s) sent successfully", response.getBody());
    }

    @Test
    @WithMockUser(username = "colaborador", roles = {"COLABORADOR"})
    void deveRetornarFormularioDeFeedbackPorId() throws Exception {

        UUID id = UUID.randomUUID();
        List<String> questions = Arrays.asList("Pergunta 1", "Pergunta 2");
        List<String> appraisers = Arrays.asList("appraiser1@example.com", "appraiser2@example.com");

        FeedbackFormDTO formDTO = new FeedbackFormDTO(id, questions, appraisers, null, null, null);

        when(serviceMock.getFeedbackFormById(id)).thenReturn(formDTO);

        mockMvc.perform(get("/form/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.questions").isArray())
                .andExpect(jsonPath("$.questions.length()").value(2))
                .andExpect(jsonPath("$.questions[0]").value("Pergunta 1"))
                .andExpect(jsonPath("$.questions[1]").value("Pergunta 2"))
                .andExpect(jsonPath("$.appraisers").isArray())
                .andExpect(jsonPath("$.appraisers.length()").value(2))
                .andExpect(jsonPath("$.appraisers[0]").value("appraiser1@example.com"))
                .andExpect(jsonPath("$.appraisers[1]").value("appraiser2@example.com"));

        verify(serviceMock, times(1)).getFeedbackFormById(id);
    }

    @Test
    void testReviewFeedbackRequest_Approve() throws Exception {
        UUID id = UUID.randomUUID();
        FeedbackRequest feedbackRequest = new FeedbackRequest(1L);
        feedbackRequest.setId(id);

        ReviewFeedbackRequestDTO reviewDTO = new ReviewFeedbackRequestDTO();
        reviewDTO.setApproved(true);
        reviewDTO.setRejectMessage(null);

        when(serviceMock.reviewFeedbackRequest(eq(id), eq(true), isNull()))
                .thenReturn(feedbackRequest);

        mockMvc.perform(put("/{id}/review", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk());

        verify(serviceMock).reviewFeedbackRequest(id, true, null);
    }

    @Test
    void testReviewFeedbackRequest_Reject() throws Exception {
        UUID id = UUID.randomUUID();
        FeedbackRequest feedbackRequest = new FeedbackRequest(1L);
        feedbackRequest.setId(id);

        String rejectMessage = "This feedback request needs improvement";

        ReviewFeedbackRequestDTO reviewDTO = new ReviewFeedbackRequestDTO();
        reviewDTO.setApproved(false);
        reviewDTO.setRejectMessage(rejectMessage);

        when(serviceMock.reviewFeedbackRequest(eq(id), eq(false), eq(rejectMessage)))
                .thenReturn(feedbackRequest);

        mockMvc.perform(put("/{id}/review", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk());

        verify(serviceMock).reviewFeedbackRequest(id, false, rejectMessage);
    }

    @Test
    void testReviewFeedbackRequest_ValidationFailure_ApproveWithMessage() throws Exception {
        UUID id = UUID.randomUUID();

        ReviewFeedbackRequestDTO reviewDTO = new ReviewFeedbackRequestDTO();
        reviewDTO.setApproved(true);
        reviewDTO.setRejectMessage("This shouldn't be here for an approval");

        mockMvc.perform(put("/{id}/review", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.valid").value("Validation failed for feedback request"));
    }

    @Test
    void testReviewFeedbackRequest_ValidationFailure_RejectWithoutMessage() throws Exception {
        UUID id = UUID.randomUUID();

        ReviewFeedbackRequestDTO reviewDTO = new ReviewFeedbackRequestDTO();
        reviewDTO.setApproved(false);
        reviewDTO.setRejectMessage("");

        mockMvc.perform(put("/{id}/review", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.valid").value("Validation failed for feedback request"));
    }

    @Test
    void testReviewFeedbackRequest_ValidationFailure_MissingApprovalStatus() throws Exception {
        UUID id = UUID.randomUUID();

        ReviewFeedbackRequestDTO reviewDTO = new ReviewFeedbackRequestDTO();
        reviewDTO.setApproved(null);
        reviewDTO.setRejectMessage("Some message");

        mockMvc.perform(put("/{id}/review", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.approved").value("Approval status is required"));
    }

    @Test
    void testReviewFeedbackRequest_RejectMessageTooLong() throws Exception {
        UUID id = UUID.randomUUID();

        // Create a reject message that exceeds 500 characters
        String longMessage = "a".repeat(501);

        ReviewFeedbackRequestDTO reviewDTO = new ReviewFeedbackRequestDTO();
        reviewDTO.setApproved(false);
        reviewDTO.setRejectMessage(longMessage);

        mockMvc.perform(put("/{id}/review", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.rejectMessage").value("Reject message cannot exceed 500 characters"));
    }

    @Test
    void testReviewFeedbackRequest_ResourceNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        ReviewFeedbackRequestDTO reviewDTO = new ReviewFeedbackRequestDTO();
        reviewDTO.setApproved(true);
        reviewDTO.setRejectMessage(null);

        when(serviceMock.reviewFeedbackRequest(eq(id), eq(true), isNull()))
                .thenThrow(new ResourceNotFoundException("Feedback request not found with id: " + id));

        FeedbackRequestController controller = new FeedbackRequestController(serviceMock);

        MockMvc localMockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        localMockMvc.perform(put("/{id}/review", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Feedback request not found with id: " + id));
    }

    @Test
    void testSendEmail_Success() throws Exception {
        FeedbackRequest feedbackRequest = new FeedbackRequest(1L);
        feedbackRequest.addAppraiser("appraiser@example.com");

        doNothing().when(serviceMock).sendEmailsForFeedbackRequest(any(FeedbackRequest.class));

        mockMvc.perform(post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(feedbackRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Email(s) sent successfully"));

        verify(serviceMock).sendEmailsForFeedbackRequest(any(FeedbackRequest.class));
    }

    @Test
    void testListFeedbacks_EmptyList() throws Exception {
        when(serviceMock.listFeedbacks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/feedbacklist"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(serviceMock).listFeedbacks();
    }

    @Test
    @WithMockUser(username = "colaborador", roles = {"COLABORADOR"})
    void deveRetornar404QuandoFormularioNaoEncontrado() throws Exception {

        UUID id = UUID.randomUUID();

        when(serviceMock.getFeedbackFormById(id))
                .thenThrow(new ResourceNotFoundException("Feedback request not found with id: " + id));

        FeedbackRequestController controller = new FeedbackRequestController(serviceMock);

        MockMvc localMockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        localMockMvc.perform(get("/form/{id}", id.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Feedback request not found with id: " + id));

        verify(serviceMock, times(1)).getFeedbackFormById(id);
    }

    @Test
    void getUsersFromPdm_WithValidToken_ShouldReturnFeedbacks() throws Exception {
        // Arrange
        String authToken = "valid_token";
        String bearerToken = "Bearer " + authToken;
        // Create sample user data
        UserInfoDTO user1 = new UserInfoDTO(101L, "john@example.com", "DEVELOPER", "SENIOR");
        UserInfoDTO user2 = new UserInfoDTO(102L, "jane@example.com", "DEVELOPER", "JUNIOR");
        List<UserInfoDTO> users = Arrays.asList(user1, user2);
        // Create sample feedback data
        PDMFeedbackDTO feedback1 = new PDMFeedbackDTO();
        feedback1.setRequester(user1);
        feedback1.setStatus("Em aprovação");
        feedback1.setCreatedAt(LocalDateTime.now());
        PDMFeedbackDTO feedback2 = new PDMFeedbackDTO();
        feedback2.setRequester(user2);
        feedback2.setStatus("Aguardando respostas");
        feedback2.setCreatedAt(LocalDateTime.now().minusDays(1));
        List<PDMFeedbackDTO> feedbacks = Arrays.asList(feedback1, feedback2);
        // Mock service behavior
        when(serviceMock.getCollaboratorsByPdm(authToken)).thenReturn(users);
        when(serviceMock.getFeedbackRequestsByUsers(users)).thenReturn(feedbacks);
        // Act & Assert
        mockMvc.perform(get("/pdm/feedbacklist")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].requester.id").value(101))
                .andExpect(jsonPath("$[0].requester.email").value("john@example.com"))
                .andExpect(jsonPath("$[0].status").value("Em aprovação"))
                .andExpect(jsonPath("$[1].requester.id").value(102))
                .andExpect(jsonPath("$[1].requester.email").value("jane@example.com"))
                .andExpect(jsonPath("$[1].status").value("Aguardando respostas"));
        // Verify service methods were called with correct parameters
        verify(serviceMock).getCollaboratorsByPdm(authToken);
        verify(serviceMock).getFeedbackRequestsByUsers(users);
    }
        @Test
        void testNotifyPDM_Success() throws Exception {
                UUID feedbackRequestId = UUID.randomUUID();
                FeedbackRequest mockFeedbackRequest = new FeedbackRequest(1L);
                
                when(serviceMock.findById(feedbackRequestId)).thenReturn(mockFeedbackRequest);
                doNothing().when(serviceMock).notifyPDMtoApproveFeedbackRequest(
                        eq(mockFeedbackRequest), 
                        anyString()
                );

                MockMvc standaloneSetup = MockMvcBuilders.standaloneSetup(feedbackRequestController)
                        .build();
                standaloneSetup.perform(post("/{feedbackRequestId}/notify-pdm", feedbackRequestId)
                        .header("Authorization", "Bearer fake-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
                verify(serviceMock, times(1)).findById(feedbackRequestId);
                verify(serviceMock, times(1)).notifyPDMtoApproveFeedbackRequest(
                        eq(mockFeedbackRequest), 
                        eq("fake-jwt-token")
                );
        }

        @Test
        void testNotifyPDM_FeedbackRequestNotFound() throws Exception {
                UUID feedbackRequestId = UUID.randomUUID();
                
                when(serviceMock.findById(feedbackRequestId)).thenReturn(null);
                
                MockMvc standaloneSetup = MockMvcBuilders.standaloneSetup(feedbackRequestController)
                        .build();
                
                standaloneSetup.perform(post("/{feedbackRequestId}/notify-pdm", feedbackRequestId)
                        .header("Authorization", "Bearer fake-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
                
                verify(serviceMock, times(1)).findById(feedbackRequestId);
                verify(serviceMock, never()).notifyPDMtoApproveFeedbackRequest(any(), anyString());
        }

        @Test
        void testNotifyPDM_MissingAuthHeader() throws Exception {
                UUID feedbackRequestId = UUID.randomUUID();
    
                MockMvc standaloneSetup = MockMvcBuilders.standaloneSetup(feedbackRequestController)
                        .build();
                
                standaloneSetup.perform(post("/{feedbackRequestId}/notify-pdm", feedbackRequestId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
                
                verify(serviceMock, never()).findById(any());
                verify(serviceMock, never()).notifyPDMtoApproveFeedbackRequest(any(), anyString());
        }

        @Test
        void testNotifyPDM_Unauthorized() throws Exception {
                UUID feedbackRequestId = UUID.randomUUID();
                
                MockMvc standaloneSetup = MockMvcBuilders.standaloneSetup(feedbackRequestController)
                        .setControllerAdvice(new ResponseStatusExceptionHandler())
                        .build();
                
                when(serviceMock.findById(any()))
                        .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                
                standaloneSetup.perform(post("/{feedbackRequestId}/notify-pdm", feedbackRequestId)
                        .header("Authorization", "Bearer fake-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
                
                verify(serviceMock).findById(any());
                verify(serviceMock, never()).notifyPDMtoApproveFeedbackRequest(any(), anyString());
        }
    @Test
    void getUsersFromPdm_WithMissingToken_ShouldThrowException() throws Exception {
        // Act & Assert - no Authorization header
        mockMvc.perform(get("/pdm/feedbacklist"))
                .andExpect(status().isBadRequest())  // Assuming InsufficientDataException maps to 400 Bad Request
                .andExpect(jsonPath("$.message").value(containsString("No valid token provided")));
        // Verify service methods were not called
        verify(serviceMock, never()).getCollaboratorsByPdm(anyString());
        verify(serviceMock, never()).getFeedbackRequestsByUsers(anyList());
    }

}