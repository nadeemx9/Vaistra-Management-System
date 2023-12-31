package com.vaistra.dto;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer userId;

    @NotEmpty(message = "Email Should not be Empty!")
    @NotNull(message = "Email should not be null!")
    @Email(message = "Invalid Email!")
    private String email;

    @NotNull(message = "Password Name should not be null!")
    @NotEmpty(message = "Password Should not be Empty!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password Must be 8 characters Long and contain a-z,A-Z,Special Character and Numbers")
    private String password;

    @NotNull(message = "First Name should not be null!")
    @NotEmpty(message = "First name should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{2,}$", message = "First name must contain only alphabets with at least 2 characters")
    private String firstName;

    @NotNull(message = "Last Name should not be null!")
    @NotEmpty(message = "First name should not be null!")
    @Pattern(regexp = "^[a-zA-Z ]{2,}$", message = "Last name must contain only alphabets with at least 2 characters")
    private String lastName;

}
