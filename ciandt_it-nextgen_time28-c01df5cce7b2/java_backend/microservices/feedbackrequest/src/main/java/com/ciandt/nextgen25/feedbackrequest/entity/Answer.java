package com.ciandt.nextgen25.feedbackrequest.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter @Setter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Column(name = "appraiser_email", nullable = false)
    private String appraiserEmail;

    @Column(name = "text", nullable = false)
    private String text;

}