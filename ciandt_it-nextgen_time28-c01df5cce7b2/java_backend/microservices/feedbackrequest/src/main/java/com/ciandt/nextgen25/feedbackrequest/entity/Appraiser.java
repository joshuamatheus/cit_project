package com.ciandt.nextgen25.feedbackrequest.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter @ToString
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Appraiser {

    @Column(name = "email", nullable = false)
    private String email;

}