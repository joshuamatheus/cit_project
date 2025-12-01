package com.ciandt.nextgen25.feedbackrequest.controller;


import com.ciandt.nextgen25.feedbackrequest.dtos.*;
import com.ciandt.nextgen25.feedbackrequest.entity.*;
import com.ciandt.nextgen25.feedbackrequest.service.*;
import com.ciandt.nextgen25.feedbackrequest.exceptions.*;
import com.ciandt.nextgen25.security.entity.LoggedUser;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;
import org.springframework.http.*;
import java.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class FeedbackRequestController {

    private final FeedbackRequestService service;

    @PostMapping("/{feedbackRequestId}/notify-pdm")
    public ResponseEntity<Void> notifyPDM(@PathVariable UUID feedbackRequestId, @RequestHeader("Authorization") String authorizationHeader) {
        FeedbackRequest feedbackRequest = service.findById(feedbackRequestId);
        if (feedbackRequest == null) {
            return ResponseEntity.notFound().build();
        }
        String token = authorizationHeader.substring(7);
        service.notifyPDMtoApproveFeedbackRequest(feedbackRequest, token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @GetMapping("/{requesterId}")
    public ResponseEntity<List<FeedbackRequestCiandTDTO>> getFeedbackRequestByRequesterId(@PathVariable Long requesterId) {
        List<FeedbackRequestCiandTDTO> feedbackRequests = service.getFeedbackRequestByRequesterId(requesterId);

        return ResponseEntity.ok(feedbackRequests);
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<String> askFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        service.sendEmailsForFeedbackRequest(feedbackRequest);
        return new ResponseEntity<>("Email(s) sent successfully", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<FeedbackRequestDTO> createFeedbackRequest(
            @RequestBody @Valid CreateFeedbackRequestDTO requestDTO,
            @AuthenticationPrincipal LoggedUser loggedUser) {

        Long requesterId = loggedUser.getId();

        FeedbackRequest createdFeedbackRequest = service.createFeedbackRequest(
                requesterId,
                requestDTO.questions(),
                requestDTO.appraiserEmails()
        );

        FeedbackRequestDTO responseDTO = new FeedbackRequestDTO(createdFeedbackRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @GetMapping("/feedbacklist")
    public ResponseEntity<List<FeedbackDTO>> listFeedbacks() {
        List<FeedbackDTO> feedbacks = service.listFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/form/{id}")
    public ResponseEntity<FeedbackFormDTO> getFeedbackForm(@PathVariable UUID id) {
        FeedbackFormDTO formDTO = service.getFeedbackFormById(id);
        return ResponseEntity.ok(formDTO);
    }


    @PutMapping("/{id}/review")
    @Transactional
    public ResponseEntity<FeedbackRequestDTO> reviewFeedbackRequest(
            @PathVariable UUID id,
            @RequestBody @Valid ReviewFeedbackRequestDTO reviewDTO) {

        FeedbackRequest updatedRequest = service.reviewFeedbackRequest(
                id,
                reviewDTO.getApproved(),
                reviewDTO.getRejectMessage()
        );

        FeedbackRequestDTO responseDTO = new FeedbackRequestDTO(updatedRequest);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/pdm/feedbacklist")
    public ResponseEntity<List<PDMFeedbackDTO>> getUsersFromPdm(@RequestHeader(value = "Authorization", required = false) String authHeader) throws JsonProcessingException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InsufficientDataException("No valid token provided");
        }
        String token = authHeader.replace("Bearer ", "");
        List<UserInfoDTO> users = service.getCollaboratorsByPdm(token);
        List<PDMFeedbackDTO> feedbacks = service.getFeedbackRequestsByUsers(users);
        return ResponseEntity.ok(feedbacks);

    }
}
