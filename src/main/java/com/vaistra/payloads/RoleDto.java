package com.vaistra.payloads;

import com.vaistra.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

public class RoleDto {

    private Integer roleId;

    @NotEmpty(message = "Role Should not be Empty!")
    @NotBlank(message = "Role Should not be Blank!")
    @Size(min = 3, message = "Role name should be at least 3 characters!")
    private String roleName;



    public RoleDto() {
    }

    public RoleDto(Integer roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }



}
