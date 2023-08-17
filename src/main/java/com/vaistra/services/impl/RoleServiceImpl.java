package com.vaistra.services.impl;

import com.vaistra.entities.Role;
import com.vaistra.entities.User;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.payloads.RoleDto;
import com.vaistra.payloads.UserDto;
import com.vaistra.repositories.RoleRepository;
import com.vaistra.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    //    -------------------------------------------------UTILITY METHODS----------------------------------------------
    public static RoleDto roleToDto(Role role)
    {
        return new RoleDto(role.getRoleId(), role.getRoleName());
    }
    public static Role dtoToRole(RoleDto roleDto)
    {
        return new Role(roleDto.getRoleId(), roleDto.getRoleName());
    }


    public static Set<RoleDto> setOfRolesToSetOfRolesDto(Set<Role> roles)
    {
        Set<RoleDto> roleDtos = new HashSet<>();
        for(Role role : roles)
        {
            roleDtos.add(new RoleDto(role.getRoleId(),role.getRoleName()));
        }
        return roleDtos;
    }
    public static Set<Role> setOfRolesDtoToSetOfRoles(Set<RoleDto> dtos)
    {
        Set<Role> roles = new HashSet<>();
        for(RoleDto dto : dtos)
        {
            roles.add(new Role(dto.getRoleId(),dto.getRoleName()));
        }
        return roles;
    }

    public static List<RoleDto> rolesToDtos(List<Role> roles)
    {
        List<RoleDto> dto = new ArrayList<>();
        for (Role r : roles)
        {
            dto.add(new RoleDto(r.getRoleId(), r.getRoleName()));
        }
        return dto;
    }


    //----------------------------------------------------SERVICE METHODS-----------------------------------------------
    @Override
    public RoleDto addRole(RoleDto roleDto) {
        Role role = new Role();
        role.setRoleName("ROLE_"+roleDto.getRoleName().toUpperCase());

        return roleToDto(roleRepository.save(role));
    }

    @Override
    public RoleDto getRoleById(int id) {
        return roleToDto(roleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Role with id '"+id+"' not found!")));
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return null;
    }

    @Override
    public String deleteRoleById(int id) {
        return null;
    }
}
