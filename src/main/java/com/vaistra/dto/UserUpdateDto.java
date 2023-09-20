package com.vaistra.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private Integer userId;

    @Email(message = "Invalid Email!")
    @Pattern(regexp = ".+", message = "Invalid Email!")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password Must be 8 characters Long and contain a-z,A-Z,Special Character and Numbers")
    private String password;

    @Pattern(regexp = "^[a-zA-Z ]{2,}$", message = "First name must contain only alphabets with at least 2 characters")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z ]{2,}$", message = "Last name must contain only alphabets with at least 2 characters")
    private String lastName;

}
