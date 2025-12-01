package com.ciandt.nextgen25.usermanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListUserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String userType;
}
