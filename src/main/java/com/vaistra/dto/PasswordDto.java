package com.vaistra.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordDto {

    @NotEmpty(message = "Email Should not be Empty!")
    @NotNull(message = "Email Should not be Null!")
    @Email(message = "Invalid Email!")
    private String email;

    @NotEmpty(message = "Password Should not be Empty!")
    @NotNull(message = "Password Should not be Null!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password Must be 8 characters Long and contain a-z,A-Z,Special Character and Numbers")
    private String newPassword;
}
