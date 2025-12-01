package com.ciandt.nextgen25.feedbackrequest.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter @Setter @ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Question {
    @Column(name = "text", nullable = false)
    private String text;
}