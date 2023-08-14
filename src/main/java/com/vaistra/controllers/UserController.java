package com.vaistra.controllers;

import com.vaistra.payloads.UserDto;
import com.vaistra.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto)
    {
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
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
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable int userId)
    {
        return new ResponseEntity<>(userService.updateUser(userDto, userId), HttpStatus.OK);
    }

    @PutMapping("softDelete/{userId}")
    public ResponseEntity<String> softDeleteUserById(@PathVariable int userId)
    {
        return new ResponseEntity<>(userService.softDeleteUserById(userId), HttpStatus.OK);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<String> hardDeleteUserById(@PathVariable int userId)
    {
        return new ResponseEntity<>(userService.hardDeleteUserById(userId), HttpStatus.OK);
    }

    @PutMapping("restore/{userId}")
    public ResponseEntity<String> restoreUserById(@PathVariable int userId)
    {
        return new ResponseEntity<>(userService.restoreUserbyId(userId), HttpStatus.OK);
    }

}
