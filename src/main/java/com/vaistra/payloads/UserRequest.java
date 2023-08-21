package com.vaistra.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequest {
    private Integer userId;

    @NotEmpty(message = "Username Should not be Empty!")
    @NotBlank(message = "Username Should not be Blank!")
    @Size(min = 3, message = "Username name should be at least 3 characters!")
    private String userName;

    @NotEmpty(message = "Password Should not be Empty!")
    @NotBlank(message = "Password Should not be Blank!")
    @Size(min = 3, message = "Password name should be at least 3 characters!")
    private String password;

    private String firstName;

    private String lastName;

    @Email(message = "Invalid Email!")
    @NotEmpty(message = "Email Should not be Empty!")
    @NotBlank(message = "Email Should not be Blank!")
    private String email;

    private String phoneNumber;

    private String gender;

    private String address;

    private Set<String> roles;
}
