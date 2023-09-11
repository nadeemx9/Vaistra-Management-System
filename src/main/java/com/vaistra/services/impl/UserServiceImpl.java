package com.vaistra.services.impl;

import com.vaistra.entities.Confirmation;
import com.vaistra.entities.User;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.payloads.UserDto;
import com.vaistra.repositories.ConfirmationRepository;
import com.vaistra.repositories.UserRepository;
import com.vaistra.services.EmailService;
import com.vaistra.services.UserService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppUtils appUtils;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AppUtils appUtils,
                           ConfirmationRepository confirmationRepository, EmailService emailService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationRepository = confirmationRepository;
        this.emailService = emailService;
        this.appUtils = appUtils;
    }


    //----------------------------------------------------SERVICE METHODS-----------------------------------------------
    @Override
    public UserDto addUser(UserDto userDto) {

        userDto.setEmail(userDto.getEmail().trim());
        if(userRepository.existsByEmailIgnoreCase(userDto.getEmail()))
            throw new DuplicateEntryException("User with email '"+userDto.getEmail()+"' already exist!");

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setStatus(true);

        User newUser = userRepository.save(user);

        Confirmation confirmation = new Confirmation(newUser);
        confirmationRepository.save(confirmation);

       /* TODO : Send email to user with Token */
//            emailService.sendSimpleMailMessage(newUser.getEmail(), newUser.getEmail(), confirmation.getToken());
        return appUtils.userToDto(newUser);
    }

    @Override
    public UserDto getUserById(int id) {
        return appUtils.userToDto(userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User with id '"+id+"' not found!")));
    }

    @Override
    public List<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> pageUser = userRepository.findAll(pageable);

        return appUtils.usersToDtos(pageUser.getContent());
    }

    @Override
    public UserDto updateUser(UserDto userDto, int id) {
        User user = userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User with id '"+id+"' not found!"));

//        User userWithSameEmail = userRepository.findByEmail(userDto.getEmail());
//        if(userWithSameEmail != null && !userWithSameEmail.getUserId() == (user.getUserId())){
//            throw new DuplicateEntryException("User with username '"+userDto.getEmail()+"' already exist!");
//        }

        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return appUtils.userToDto(userRepository.save(user));
    }


    @Override
    public String hardDeleteUserById(int id) {
        userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User with id '"+id+"' not found!"));

        userRepository.deleteById(id);
        return "User with id  "+id+"' deleted!";
    }

    @Override
    public Boolean verifyToken(String token) {

        Confirmation confirmation = confirmationRepository.findByToken(token);
        if(confirmation == null)
            throw new ResourceNotFoundException("Confirmation with token '"+token+"' not found");

        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setStatus(true);
        userRepository.save(user);
        return Boolean.TRUE;
    }
}
