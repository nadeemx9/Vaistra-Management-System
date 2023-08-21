package com.vaistra.services;

import com.vaistra.entities.User;
import com.vaistra.payloads.UserDto;
import com.vaistra.payloads.UserRequest;

import java.util.List;

public interface UserService {
    UserDto addUser(UserRequest userRequest);
    UserDto getUserById(int id);
    List<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection);
    UserDto updateUser(UserDto userDto, int id);
    String softDeleteUserById(int id);
    String hardDeleteUserById(int id);
    String restoreUserById(int id);
    Boolean verifyToken(String token);
}
