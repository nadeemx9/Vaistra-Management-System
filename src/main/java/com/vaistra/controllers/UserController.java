package com.vaistra.controllers;

import com.vaistra.dto.PasswordDto;
import com.vaistra.dto.UserDto;
import com.vaistra.dto.UserUpdateDto;
import com.vaistra.entities.User;
import com.vaistra.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }


    //---------------------------------------------------URL ENDPOINTS--------------------------------------------------
    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto)
    {
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping("verify")
    public ResponseEntity<String> verify(@RequestParam("token") String token)
    {
        Boolean isSuccess = userService.verifyToken(token);
        return null;
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable int userId)
    {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                    @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = "userId", required = false) String sortBy,
                                                    @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(userService.getAllUsers(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @PutMapping("{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserUpdateDto userDto, @PathVariable int userId)
    {
        return new ResponseEntity<>(userService.updateUser(userDto, userId), HttpStatus.OK);
    }

    @PostMapping("forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody PasswordDto passwordDto)
    {
        return new ResponseEntity<>(userService.forgotPassword(passwordDto), HttpStatus.OK);
    }
    @GetMapping("reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword)
    {
        return new ResponseEntity<>(userService.resetPassword(token, newPassword), HttpStatus.OK);
    }
}
