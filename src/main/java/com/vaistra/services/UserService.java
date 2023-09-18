package com.vaistra.services;

import com.vaistra.dto.PasswordDto;
import com.vaistra.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);
    UserDto getUserById(int id);
    List<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection);
    UserDto updateUser(UserDto userDto, int id);

    String hardDeleteUserById(int id);

    Boolean verifyToken(String token);

    String forgotPassword(UserDto password, int userId, UserDetails loggedInUser);
    String resetPassword(int userId, UserDetails loggedInUser, String password);
}
