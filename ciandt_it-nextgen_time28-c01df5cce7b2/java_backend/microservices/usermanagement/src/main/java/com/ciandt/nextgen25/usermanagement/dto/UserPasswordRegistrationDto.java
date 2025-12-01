package com.ciandt.nextgen25.usermanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserPasswordRegistrationDto {
    
    @NotBlank
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
             message = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace")
    private String password;

    @NotBlank
    private String confirmPassword;

    @AssertTrue(message = "Password and Confirm Password must match")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}



