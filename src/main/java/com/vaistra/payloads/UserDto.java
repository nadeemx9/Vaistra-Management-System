package com.vaistra.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

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
    @NotEmpty(message = "Role Should not be Empty!")
    @NotBlank(message = "Role Should not be Blank!")
    @Size(min = 3, message = "Role name should be at least 3 characters!")
    private String role;

    private boolean deleted;

    public UserDto() {
    }

    public UserDto(int userId, String userName, String password, String role, boolean deleted) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
