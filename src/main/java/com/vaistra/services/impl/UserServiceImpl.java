package com.vaistra.services.impl;

import com.vaistra.entities.Country;
import com.vaistra.entities.User;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.payloads.CountryDto;
import com.vaistra.payloads.UserDto;
import com.vaistra.repositories.UserRepository;
import com.vaistra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //    -------------------------------------------------UTILITY METHODS----------------------------------------------
    public static UserDto userToDto(User user) {
        return new UserDto(user.getUserId(), user.getUserName(), user.getPassword(), user.getRole(), user.isDeleted());
    }

    public static User dtoToUser(UserDto dto) {
        return new User(dto.getUserId(), dto.getUserName(), dto.getPassword(), dto.getRole(), dto.isDeleted());
    }

    public static List<UserDto> usersToDtos(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(new UserDto(u.getUserId(), u.getUserName(), u.getPassword(), u.getRole(), u.isDeleted()));
        }
        return dtos;
    }

    //----------------------------------------------------SERVICE METHODS-----------------------------------------------
    @Override
    public UserDto addUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userToDto(userRepository.save(dtoToUser(userDto)));
    }

    @Override
    public UserDto getUserById(int id) {
        return userToDto(userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User with id '"+id+"' not found!")));
    }

    @Override
    public List<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> pageUser = userRepository.findAll(pageable);
        return usersToDtos(pageUser.getContent());
    }

    @Override
    public UserDto updateUser(UserDto userDto, int id) {
        User user = userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User with id '"+id+"' not found!"));

        user.setUserName(userDto.getUserName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        return userToDto(userRepository.save(dtoToUser(userDto)));
    }

    @Override
    public String softDeleteUserById(int id) {
        User user = userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User with id '"+id+"' not found!"));
        user.setDeleted(true);

        userRepository.save(user);
        return "User with id '"+id+"' deleted!";
    }

    @Override
    public String hardDeleteUserById(int id) {
        userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User with id '"+id+"' not found!"));

        userRepository.deleteById(id);
        return "User with id  "+id+"' deleted!";
    }

    @Override
    public String restoreUserbyId(int id) {
        User user = userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User with id '"+id+"' not found!"));
        user.setDeleted(false);
        userRepository.save(user);
        return "User with id '"+id+"' restored!";
    }
}
