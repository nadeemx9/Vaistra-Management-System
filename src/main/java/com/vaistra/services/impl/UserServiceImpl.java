package com.vaistra.services.impl;

import com.vaistra.entities.Confirmation;
import com.vaistra.entities.Country;
import com.vaistra.entities.Role;
import com.vaistra.entities.User;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.payloads.CountryDto;
import com.vaistra.payloads.RoleDto;
import com.vaistra.payloads.UserDto;
import com.vaistra.payloads.UserRequest;
import com.vaistra.repositories.ConfirmationRepository;
import com.vaistra.repositories.RoleRepository;
import com.vaistra.repositories.UserRepository;
import com.vaistra.services.EmailService;
import com.vaistra.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final HttpServletRequest request;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                           HttpServletRequest request, ConfirmationRepository confirmationRepository, EmailService emailService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.request = request;
        this.confirmationRepository = confirmationRepository;
        this.emailService = emailService;
    }

    //    -------------------------------------------------UTILITY METHODS----------------------------------------------
    public static UserDto userToDto(User user) {
        return new UserDto(user.getUserId(), user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPhoneNumber(), user.getGender(), user.getAddress(), user.isStatus(), user.getCreatedAt(),
                user.isDeleted(), user.getLastLogin(), user.getLastAccessIp(), RoleServiceImpl.setOfRolesToSetOfRolesDto(user.getRoles()));


    }

    public static User dtoToUser(UserDto dto) {
        return new User(dto.getUserId(), dto.getUserName(), dto.getPassword(), dto.getFirstName(), dto.getLastName(), dto.getEmail(),
                dto.getPhoneNumber(), dto.getGender(), dto.getAddress(), dto.isStatus(), dto.getCreatedAt(), dto.isDeleted(), dto.getLastLogin(),
                dto.getLastAccessIp(), RoleServiceImpl.setOfRolesDtoToSetOfRoles(dto.getRoles()));
    }

    public static List<UserDto> usersToDtos(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(new UserDto(u.getUserId(), u.getUsername(), u.getPassword(), u.getFirstName(), u.getLastName(), u.getEmail(),
                    u.getPhoneNumber(), u.getGender(), u.getAddress(), u.isStatus(), u.getCreatedAt(), u.isDeleted(), u.getLastLogin(),
                    u.getLastAccessIp(), RoleServiceImpl.setOfRolesToSetOfRolesDto(u.getRoles())));
        }
        return dtos;
    }
    public static Set<UserDto> setOfUsersToSetOfUsersDto(Set<User> users)
    {
        Set<UserDto> dto = new HashSet<>();
        for (User u : users)
        {
            dto.add(new UserDto(u.getUserId(), u.getUsername(), u.getPassword(), u.getFirstName(), u.getLastName(), u.getEmail(),
                    u.getPhoneNumber(), u.getGender(), u.getAddress(), u.isStatus(), u.getCreatedAt(), u.isDeleted(), u.getLastLogin(),
                    u.getLastAccessIp(), RoleServiceImpl.setOfRolesToSetOfRolesDto(u.getRoles())));
        }
        return dto;
    }
    public static Set<User> setOfUsersDtoToSetOfUsers(Set<UserDto> dto)
    {
        Set<User> users = new HashSet<>();
        for (UserDto d : dto)
        {
            users.add(new User(d.getUserId(), d.getUserName(), d.getPassword(), d.getFirstName(), d.getLastName(), d.getEmail(),
                    d.getPhoneNumber(), d.getGender(), d.getAddress(), d.isStatus(), d.getCreatedAt(),d.isDeleted(),d.getLastLogin(),
                    d.getLastAccessIp(), RoleServiceImpl.setOfRolesDtoToSetOfRoles(d.getRoles())));
        }
        return users;
    }


    //----------------------------------------------------SERVICE METHODS-----------------------------------------------
    @Override
    public UserDto addUser(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail()))
            throw new DuplicateEntryException("User with email '"+userRequest.getEmail()+"' already exist!");

        try {
            if(userRequest.getRoles().isEmpty())
                userRequest.getRoles().add("user");
        }catch (Exception ex){

        }

        Set<Role> roles = new HashSet<>();

        for(String s : userRequest.getRoles())
        {
             s = "ROLE_"+s.toUpperCase();
            Role role = roleRepository.findByRoleName(s);
            if(role != null) {
                roles.add(role);
            }
            else
                throw new ResourceNotFoundException("Role with name '"+s+"' not found");
        }
        User user = new User();
        user.setUserName(userRequest.getUserName().toUpperCase());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setGender(userRequest.getGender());
        user.setAddress(userRequest.getAddress());
        user.setStatus(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setLastAccessIp("lastaccessip");
        user.setRoles(roles);
        User newUser = userRepository.save(user);

        Confirmation confirmation = new Confirmation(newUser);
        confirmationRepository.save(confirmation);

       /* TODO : Send email to user with Token */
            emailService.sendSimpleMailMessage(newUser.getUsername(), newUser.getEmail(), confirmation.getToken());
        return userToDto(newUser);
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
        user.setRoles(RoleServiceImpl.setOfRolesDtoToSetOfRoles(userDto.getRoles()));
        return userToDto(userRepository.save(user));
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
    public String restoreUserById(int id) {
        User user = userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User with id '"+id+"' not found!"));
        user.setDeleted(false);
        userRepository.save(user);
        return "User with id '"+id+"' restored!";
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
