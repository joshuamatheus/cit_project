package com.ciandt.nextgen25.feedbackrequest.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PDMFeedbackDTO {
    UserInfoDTO requester;
    String status;
    LocalDateTime createdAt;
}