package com.ciandt.nextgen25.feedbackrequest.entity;

import com.ciandt.nextgen25.feedbackrequest.exceptions.*;
import com.ciandt.nextgen25.feedbackrequest.service.FeedbackRequestService;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "feedback_requests")
@Getter @Setter @ToString
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
public class FeedbackRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "edited_at")
    private LocalDateTime editedAt;

    @Column(name = "reject_message", length = 500)
    private String rejectMessage;

    @ElementCollection
    @CollectionTable(
            name = "feedback_request_appraisers",
            joinColumns = @JoinColumn(name = "feedback_request_id")
    )
    private final List<Appraiser> appraisers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "feedback_request_questions",
            joinColumns = @JoinColumn(name = "feedback_request_id")
    )
    private final List<Question> questions = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "feedback_request_answers",
            joinColumns = @JoinColumn(name = "feedback_request_id")
    )
    private final List<Answer> answers = new ArrayList<>();

    public FeedbackRequest(Long requesterId) {
        if (requesterId == null) {
            throw new ValidationException("Requester ID cannot be empty");
        }
        this.requesterId = requesterId;
        this.createdAt = LocalDateTime.now();
    }

    public void addAppraiser(String email) {
        Appraiser appraiser = new Appraiser(email);
        if (appraisers.stream().anyMatch(a -> a.getEmail().equalsIgnoreCase(email))) {
            throw new DuplicateResourceException("Appraiser with email " + email + " already exists");
        }
        if (email.contains("@ciandt")) {
            throw new ValidationException("The email cannot be from an internal employee.");
        }
        appraisers.add(appraiser);
    }

    public void removeAppraiser(String email) {
        appraisers.removeIf(a -> a.getEmail().equalsIgnoreCase(email));
        answers.removeIf(a -> a.getAppraiserEmail().equalsIgnoreCase(email));
    }

    public Optional<Appraiser> findAppraiserByEmail(String email) {
        return appraisers.stream()
                .filter(a -> a.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public void addQuestion(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException("Question text cannot be empty");
        }
        questions.add(new Question(text));
    }

    public void removeQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            throw new ValidationException("Question index out of range: " + index);
        }
        questions.remove(index);
    }

    public void updateQuestion(int index, String newText) {
        if (index < 0 || index >= questions.size()) {
            throw new ValidationException("Question index out of range");
        }
        if (newText == null || newText.trim().isEmpty()) {
            throw new ValidationException("Question text cannot be empty");
        }
        questions.set(index, new Question(newText));
    }

    public void submitAnswer(String appraiserEmail, String answerText) {
        if (!isApproved()) {
            throw new IllegalStateOperationException("Cannot submit answers for unapproved feedback request");
        }
        if (isExpired()) {
            throw new ExpiredResourceException("Cannot submit answers for expired feedback request");
        }
        findAppraiserByEmail(appraiserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Appraiser not found with email: " + appraiserEmail));

        Answer answer = new Answer(appraiserEmail, answerText);
        answers.add(answer);
    }

    public void reject(String rejectMessage) {
        if (rejectMessage == null || rejectMessage.trim().isEmpty()) {
            throw new ValidationException("Reject message cannot be empty");
        }
        this.rejectedAt = LocalDateTime.now();
        this.rejectMessage = rejectMessage;
    }

    public void approve() {
        if (questions.isEmpty() || questions.size() < 3) {
            throw new InsufficientDataException("Feedback request must have at least 1 question and max 3 questions.");
        }
        if (appraisers.isEmpty()) {
            throw new InsufficientDataException("Feedback request must have at least one appraiser");
        }
        this.approvedAt = LocalDateTime.now();
    }

    public void markAsEdited() {
        this.editedAt = LocalDateTime.now();
        this.rejectedAt = null;
    }

    public boolean isApproved() {
        return approvedAt != null && (rejectedAt == null || approvedAt.isAfter(rejectedAt));
    }

    public boolean isRejected() {
        return rejectedAt != null && approvedAt == null;
    }

    public boolean isEdited() {
        return editedAt != null;
    }

    public boolean isExpired() {
        return createdAt != null && createdAt.plusMonths(3).isBefore(LocalDateTime.now());
    }

    public List<Answer> getAppraiserAnswers(String appraiserEmail) {
        return answers.stream()
                .filter(a -> a.getAppraiserEmail().equalsIgnoreCase(appraiserEmail))
                .collect(Collectors.toList());
    }

    public Map<String, String> getAppraisersWithStatus() {
        Map<String, String> appraisersStatus = new HashMap<>();

        if (approvedAt == null) {
            for (Appraiser appraiser : appraisers) {
                appraisersStatus.put(appraiser.getEmail(), "Não Enviado");
            }
            return appraisersStatus;
        }

        for (Appraiser appraiser : appraisers) {
            String email = appraiser.getEmail();

            boolean hasAnswered = answers.stream()
                    .anyMatch(answer -> answer.getAppraiserEmail().equalsIgnoreCase(email));

            if (hasAnswered) {
                appraisersStatus.put(email, "Respondido");
            } else {
                appraisersStatus.put(email, "Não Respondido");
            }
        }

        return appraisersStatus;
    }

    public String getRequestStatus() {
        if (isRejected()) {
            return FeedbackRequestStatus.REJECTED_STATUS.getStatus();
        } else if (isExpired()) {
            return FeedbackRequestStatus.EXPIRED_STATUS.getStatus();
        } else if (isApproved()) {
            if (!answers.isEmpty() && hasAllAppraisersAnswered()) {
                return FeedbackRequestStatus.FINALIZED_STATUS.getStatus();
            } else {
                return FeedbackRequestStatus.WAITING_ANSWERS_STATUS.getStatus();
            }
        } else if (createdAt != null) {
            return FeedbackRequestStatus.PENDING_APPROVAL_STATUS.getStatus();
        } else {
            return FeedbackRequestStatus.IN_CREATION_STATUS.getStatus();
        }
    }

    public boolean hasAllAppraisersAnswered() {
        int questionCount = questions.size();

        Map<String, List<Answer>> answersByAppraiser = answers.stream()
                .collect(Collectors.groupingBy(answer -> answer.getAppraiserEmail().toLowerCase()));

        return appraisers.stream()
                .map(appraiser -> appraiser.getEmail().toLowerCase())
                .allMatch(email -> {
                    List<Answer> appraiserAnswers = answersByAppraiser.getOrDefault(email, Collections.emptyList());
                    return appraiserAnswers.size() >= questionCount;
                });
    }
}