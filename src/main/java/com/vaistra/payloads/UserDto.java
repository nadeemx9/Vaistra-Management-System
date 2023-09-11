package com.vaistra.payloads;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int userId;

    @NotEmpty(message = "Email Should not be Empty!")
    @NotBlank(message = "Email Should not be Blank!")
    @Email(message = "Invalid Email!")
    private String email;

    @NotEmpty(message = "Password Should not be Empty!")
    @NotBlank(message = "Password Should not be Blank!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password Must be 8 characters Long and contain a-z,A-Z,Special Character and Numbers")
    private String password;

    @NotNull(message = "First Name should not be null!")
    private String firstName;

    @NotNull(message = "Last Name should not be null!")
    private String lastName;

}
