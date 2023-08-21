package com.vaistra.controllers;

import com.vaistra.payloads.HttpResponse;
import com.vaistra.payloads.UserDto;
import com.vaistra.payloads.UserRequest;
import com.vaistra.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<HttpResponse> addUser(@Valid @RequestBody UserRequest userRequest)
    {
        UserDto user = userService.addUser(userRequest);
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.CREATED)
                        .message("User Created")
                        .data(Map.of("User", user))
                        .build()
        );
    }

    @GetMapping("verify")
    public ResponseEntity<HttpResponse> verify(@RequestParam("token") String token)
    {
        Boolean isSuccess = userService.verifyToken(token);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.OK)
                        .message("User Verified!")
                        .data(Map.of("Success", isSuccess))
                        .build()
        );
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
        return new ResponseEntity<>(userService.restoreUserById(userId), HttpStatus.OK);
    }

}
