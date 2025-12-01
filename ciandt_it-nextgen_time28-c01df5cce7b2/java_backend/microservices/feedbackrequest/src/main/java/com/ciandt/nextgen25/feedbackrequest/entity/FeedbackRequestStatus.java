package com.ciandt.nextgen25.feedbackrequest.entity;

import lombok.Getter;

@Getter
public enum FeedbackRequestStatus {
    REJECTED_STATUS("Rejeitado"),
    EXPIRED_STATUS("Expirado"),
    FINALIZED_STATUS("Finalizado"),
    WAITING_ANSWERS_STATUS("Aguardando respostas"),
    PENDING_APPROVAL_STATUS("Em aprovação"),
    IN_CREATION_STATUS("Em criação");

    private final String status;

    FeedbackRequestStatus(String status) {
        this.status = status;
    }
}
