package com.vaistra.services;

import com.vaistra.payloads.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto addRole(RoleDto role);
    RoleDto getRoleById(int id);
    List<RoleDto> getAllRoles();
    String deleteRoleById(int id);
}
