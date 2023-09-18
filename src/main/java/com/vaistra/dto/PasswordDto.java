package com.vaistra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordDto {

    @NotEmpty(message = "Email Should not be Empty!")
    @NotBlank(message = "Email Should not be Blank!")
    @Email(message = "Invalid Email!")
    private String email;

    @NotEmpty(message = "Password Should not be Empty!")
    @NotBlank(message = "Password Should not be Blank!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password Must be 8 characters Long and contain a-z,A-Z,Special Character and Numbers")
    private String newPassword;
}
