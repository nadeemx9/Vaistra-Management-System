package com.vaistra.services.impl;

import com.vaistra.entities.Role;
import com.vaistra.entities.User;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.payloads.RoleDto;
import com.vaistra.payloads.UserDto;
import com.vaistra.repositories.RoleRepository;
import com.vaistra.services.RoleService;
import com.vaistra.utils.AppUtils;
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


    //----------------------------------------------------SERVICE METHODS-----------------------------------------------
    @Override
    public RoleDto addRole(RoleDto roleDto) {
        roleDto.setRoleName("ROLE_"+roleDto.getRoleName().toUpperCase());
        Role role = roleRepository.findByRoleName(roleDto.getRoleName());
        if(role != null)
            throw new DuplicateEntryException("Role '"+roleDto.getRoleName()+"' already exist");

        return AppUtils.roleToDto(roleRepository.save(AppUtils.dtoToRole(roleDto)));
    }

    @Override
    public RoleDto getRoleById(int id) {
        return AppUtils.roleToDto(roleRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Role with id '"+id+"' not found!")));
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return AppUtils.rolesToDtos(roleRepository.findAll());
    }

    @Override
    public String deleteRoleById(int id) {
        return null;
    }
}
