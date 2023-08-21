package com.vaistra.controllers;

import com.vaistra.payloads.RoleDto;
import com.vaistra.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/role")
public class RoleController {

    private final RoleService roleService;
    @Autowired
    public RoleController(RoleService roleService)
    {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDto> addRole(@Valid @RequestBody RoleDto roleDto)
    {
        return new ResponseEntity<>(roleService.addRole(roleDto), HttpStatus.CREATED);
    }

    @GetMapping("{roleId}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable int roleId)
    {
        return new ResponseEntity<>(roleService.getRoleById(roleId), HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles()
    {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.FOUND);
    }
}
