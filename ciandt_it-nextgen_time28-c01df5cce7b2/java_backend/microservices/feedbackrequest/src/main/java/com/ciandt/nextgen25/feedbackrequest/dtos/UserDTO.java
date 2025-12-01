package com.ciandt.nextgen25.feedbackrequest.dtos;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;

    private Long pdmId;
    private String pdmName;
    private String pdmEmail;
}