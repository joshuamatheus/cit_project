package com.ciandt.nextgen25.feedbackrequest.service;

import com.ciandt.nextgen25.security.entity.LoggedUser;
import com.ciandt.nextgen25.security.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ciandt.nextgen25.feedbackrequest.dtos.*;
import com.ciandt.nextgen25.feedbackrequest.entity.*;
import com.ciandt.nextgen25.feedbackrequest.repository.FeedbackRequestRepository;
import com.ciandt.nextgen25.feedbackrequest.exceptions.*;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackRequestService {

    private final FeedbackRequestRepository repository;
    private final RestTemplate restTemplate;
    private final JwtTokenService jwtTokenService;
    private final EmailService emailService;
    private final HttpServletRequest request;

    @Value("${usermanagement.service.url}")
    private String userManagementServiceUrl;

    public List<FeedbackRequestCiandTDTO> getFeedbackRequestByRequesterId(Long requesterId) {
        List<FeedbackRequest> feedbackRequests = repository.findByRequesterId(requesterId);

        if (feedbackRequests.isEmpty()) {
            throw new ResourceNotFoundException("No Feedback Requests were found.");
        }

        return feedbackRequests.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    protected FeedbackRequestCiandTDTO mapToDTO(FeedbackRequest feedbackRequest) {
        FeedbackRequestCiandTDTO dto = new FeedbackRequestCiandTDTO();
        dto.setId(feedbackRequest.getId());
        dto.setRequesterId(feedbackRequest.getRequesterId());
        dto.setCreatedAt(feedbackRequest.getCreatedAt());
        dto.setApprovedAt(feedbackRequest.getApprovedAt());
        dto.setRejectedAt(feedbackRequest.getRejectedAt());
        dto.setEditedAt(feedbackRequest.getEditedAt());

        if (!CollectionUtils.isEmpty(feedbackRequest.getAppraisers())) {
            List<AppraiserDTO> appraisersDTO = feedbackRequest.getAppraisers().stream()
                    .map(appraiser -> new AppraiserDTO(appraiser.getEmail()))
                    .collect(Collectors.toList());
            dto.setAppraisers(appraisersDTO);
        }

        if (!CollectionUtils.isEmpty(feedbackRequest.getQuestions())) {
            List<QuestionDTO> questionsDTO = feedbackRequest.getQuestions().stream()
                    .map(question -> new QuestionDTO(question.getText()))
                    .collect(Collectors.toList());
            dto.setQuestions(questionsDTO);
        }

        if (!CollectionUtils.isEmpty(feedbackRequest.getAnswers())) {
            List<AnswerDTO> answersDTO = feedbackRequest.getAnswers().stream()
                    .map(answer -> new AnswerDTO(answer.getAppraiserEmail(), answer.getText()))
                    .collect(Collectors.toList());
            dto.setAnswers(answersDTO);
        }

        dto.setApproved(feedbackRequest.isApproved());
        dto.setRejected(feedbackRequest.isRejected());
        dto.setExpired(feedbackRequest.isExpired());

        return dto;
    }

    public void notifyPDMtoApproveFeedbackRequest(FeedbackRequest feedbackRequest, String authToken) {
        Long requesterId = feedbackRequest.getRequesterId();
        
        UserDTO user = getUserByRequesterId(requesterId, authToken);
        UserDTO pdm = getPdmByRequesterId(user);
        if (pdm == null) {
            return;
        }
    
        String subject = "Solicitação de aprovação de Feedback - Solicitação #" + feedbackRequest.getId();
        // String linkToRequest = feedbackRequest.getLink();

        StringBuilder messageBody = new StringBuilder();
        messageBody.append("Olá ").append(pdm.getName()).append(",\n\n")
                    .append("O colaborador ").append(user.getName()).append(" solicitou feedback e você precisa aprovar a solicitação #")
                    .append(feedbackRequest.getId())
                    .append(". Por favor, acesse o link abaixo para aprovar ou rejeitar a solicitação:\n\n")
                    .append("linkToRequest").append("\n\n")
                    .append("Agradecemos a sua colaboração e estamos à disposição para qualquer dúvida.\n\n")
                    .append("Atenciosamente,\n\n")
                    .append("CI&T Software S.A.");
    
        emailService.sendEmail(pdm.getEmail(), subject, messageBody.toString());
    }

    public UserDTO getPdmByRequesterId(UserDTO user){
        if (user != null && user.getPdmId() != null) {
            UserDTO pdm = new UserDTO();
            pdm.setId(user.getPdmId());
            pdm.setName(user.getPdmName());
            pdm.setEmail(user.getPdmEmail());
            return pdm;
        }
        return null;
    }

    public UserDTO getUserByRequesterId(Long requesterId, String authToken) {
        try {
            String url = userManagementServiceUrl + "/user/" + requesterId;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + authToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<UserDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserDTO.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public void sendEmailsForFeedbackRequest(FeedbackRequest feedbackRequest) {
        for (Appraiser appraiser : feedbackRequest.getAppraisers()) {
            sendEmailFromFeedback(feedbackRequest, appraiser);
        }
    }

    public void sendEmailFromFeedback(FeedbackRequest feedback, Appraiser appraiser) {
        String subject = "Pesquisa de opinião";

        String messageBody = "Olá Avaliador " + ",\n\n" +
                "Você foi selecionado para fornecer seu feedback. Por favor, responda o questionário no link:\n\n" +
                " [link para o questionário]\n\n" + "\n\n" +
                "Obrigado pela participação!";

        String recipientEmail = appraiser.getEmail();
        emailService.sendEmail(recipientEmail, subject, messageBody);
    }

    public FeedbackRequest createFeedbackRequest(
            Long requesterId, List<String> questions, List<String> appraiserEmails) {

        FeedbackRequest feedbackRequest = new FeedbackRequest(requesterId);
        appraiserEmails.forEach(feedbackRequest::addAppraiser);
        questions.forEach(feedbackRequest::addQuestion);

        String authToken = request.getHeader("Authorization");
        authToken = authToken.substring(7);
        notifyPDMtoApproveFeedbackRequest(feedbackRequest, authToken);
            
        return repository.save(feedbackRequest);
    }


    public List<FeedbackDTO> listFeedbacks() {
        List<FeedbackRequest> feedbackRequests = repository.findAll();
        return feedbackRequests.stream()
                .map(this::mapToFeedbackDTO)
                .collect(Collectors.toList());
    }

    private FeedbackDTO mapToFeedbackDTO(FeedbackRequest feedbackRequest) {
        String status = feedbackRequest.getRequestStatus();

        return new FeedbackDTO(
                feedbackRequest.getId(),
                feedbackRequest.getRequesterId(),
                feedbackRequest.getCreatedAt(),
                status
        );
    }

    public FeedbackRequest findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Feedback request not found with id: " + id));
    }

    @Transactional
    public FeedbackRequest reviewFeedbackRequest(UUID feedbackRequestId, boolean approved, String rejectMessage) {
        FeedbackRequest request = findById(feedbackRequestId);

        if (approved) {
            request.approve();
        } else {
            if (rejectMessage == null || rejectMessage.trim().isEmpty()) {
                throw new ValidationException("Reject message is required when rejecting a feedback request");
            }
            request.reject(rejectMessage);
        }

        return repository.save(request);
    }

    @Transactional
    public FeedbackFormDTO getFeedbackFormById(UUID id) {
        FeedbackRequest feedbackRequest = findById(id);

        List<String> questions = new ArrayList<>();
        List<String> appraisers = new ArrayList<>();

        if (feedbackRequest.getQuestions() != null) {
            for (var question : feedbackRequest.getQuestions()) {
                if (question != null && question.getText() != null) {
                    questions.add(question.getText());
                }
            }
        }

        if (feedbackRequest.getAppraisers() != null) {
            for (var appraiser : feedbackRequest.getAppraisers()) {
                if (appraiser != null && appraiser.getEmail() != null) {
                    appraisers.add(appraiser.getEmail());
                }
            }
        }

        Map<String, String> appraiserStatus = feedbackRequest.getAppraisersWithStatus();
        
        return new FeedbackFormDTO(
            feedbackRequest.getId(), 
            questions, 
            appraisers,
            appraiserStatus,
            feedbackRequest.getRequesterId(),
            feedbackRequest.getCreatedAt()
        );
    }

    public List<UserInfoDTO> getCollaboratorsByPdm(String authToken) throws JsonProcessingException {
        LoggedUser loggedUser = jwtTokenService.getUserFromToken(authToken);
        String url = userManagementServiceUrl + "/pdm/" + loggedUser.getId() + "/collaborators";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<UserInfoDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<UserInfoDTO>>() {}
        );

        return response.getBody();
    }

    public List<PDMFeedbackDTO> getFeedbackRequestsByUsers(List<UserInfoDTO> collaborators) {
        if (collaborators == null || collaborators.isEmpty()) {
            return Collections.emptyList();
        }

        List<PDMFeedbackDTO> result = new ArrayList<>();

        for (UserInfoDTO collaborator : collaborators) {
            try {
                List<FeedbackRequest> requests = repository.findByRequesterId(collaborator.getId());
                for (FeedbackRequest request : requests) {
                    String status = request.getRequestStatus();
                    PDMFeedbackDTO dto = new PDMFeedbackDTO(
                            collaborator,
                            status,
                            request.getCreatedAt()
                    );
                    result.add(dto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        result.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return result;
    }
}
