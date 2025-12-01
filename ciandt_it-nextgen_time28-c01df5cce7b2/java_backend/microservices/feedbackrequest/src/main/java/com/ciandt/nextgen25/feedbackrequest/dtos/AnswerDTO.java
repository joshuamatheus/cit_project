package com.ciandt.nextgen25.feedbackrequest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private String appraiserEmail;
    private String text;
}