package com.vaistra.payloads;

import com.vaistra.entities.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private int userId;

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

    private String email;

    private String phoneNumber;

    private String gender;

    private String address;

    private boolean status;

    private LocalDateTime createdAt;

    private boolean deleted = false;

    private LocalDateTime lastLogin;

    private String lastAccessIp;

    private Set<RoleDto> roles = new HashSet<>();

}
