package com.vaistra.services;

import com.vaistra.dto.PasswordDto;
import com.vaistra.dto.UserDto;
import com.vaistra.dto.UserUpdateDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);
    UserDto getUserById(int id);
    List<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection);
    UserDto updateUser(UserUpdateDto userDto, int id);

    String hardDeleteUserById(int id);

    Boolean verifyToken(String token);

    String forgotPassword(PasswordDto passwordDto);
    String resetPassword(String token, String password);
}
