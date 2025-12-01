package com.ciandt.nextgen25.usermanagement.dto;
import com.ciandt.nextgen25.usermanagement.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTypeResponseDTO {
    private UserType type;
}