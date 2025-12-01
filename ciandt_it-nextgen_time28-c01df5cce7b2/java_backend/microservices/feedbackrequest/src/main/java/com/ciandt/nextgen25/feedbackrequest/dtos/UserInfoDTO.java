package com.ciandt.nextgen25.feedbackrequest.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    Long id;
    String email;
    String role;
    String positionMap;
}