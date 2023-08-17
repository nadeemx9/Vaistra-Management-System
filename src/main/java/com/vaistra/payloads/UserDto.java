package com.vaistra.payloads;

import com.vaistra.entities.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

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
//    @NotEmpty(message = "Role Should not be Empty!")
//    @NotBlank(message = "Role Should not be Blank!")
//    @Size(min = 3, message = "Role name should be at least 3 characters!")
    private Set<RoleDto> roles = new HashSet<>();

    private boolean deleted;

    public UserDto() {
    }

    public UserDto(int userId, String userName, String password, Set<RoleDto> roles, boolean deleted) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
        this.deleted = deleted;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


}
