package com.vaistra.services.impl;

import com.vaistra.entities.Country;
import com.vaistra.entities.Role;
import com.vaistra.entities.User;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.payloads.CountryDto;
import com.vaistra.payloads.RoleDto;
import com.vaistra.payloads.UserDto;
import com.vaistra.payloads.UserRequest;
import com.vaistra.repositories.RoleRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    //    -------------------------------------------------UTILITY METHODS----------------------------------------------
    public static UserDto userToDto(User user) {
        return new UserDto(user.getUserId(), user.getUserName(), user.getPassword(), RoleServiceImpl.setOfRolesToSetOfRolesDto(user.getRoles()), user.isDeleted());
    }

    public static User dtoToUser(UserDto dto) {
        return new User(dto.getUserId(), dto.getUserName(), dto.getPassword(), RoleServiceImpl.setOfRolesDtoToSetOfRoles(dto.getRoles()), dto.isDeleted());
    }

    public static List<UserDto> usersToDtos(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(new UserDto(u.getUserId(), u.getUserName(), u.getPassword(), RoleServiceImpl.setOfRolesToSetOfRolesDto(u.getRoles()), u.isDeleted()));
        }
        return dtos;
    }
    public static Set<UserDto> setOfUsersToSetOfUsersDto(Set<User> users)
    {
        Set<UserDto> dto = new HashSet<>();
        for (User u : users)
        {
            dto.add(new UserDto(u.getUserId(), u.getUserName(), u.getPassword(), RoleServiceImpl.setOfRolesToSetOfRolesDto(u.getRoles()), u.isDeleted()));
        }
        return dto;
    }
    public static Set<User> setOfUsersDtoToSetOfUsers(Set<UserDto> dto)
    {
        Set<User> users = new HashSet<>();
        for (UserDto d : dto)
        {
            users.add(new User(d.getUserId(), d.getUserName(), d.getPassword(), RoleServiceImpl.setOfRolesDtoToSetOfRoles(d.getRoles()), d.isDeleted()));
        }
        return users;
    }

    //----------------------------------------------------SERVICE METHODS-----------------------------------------------
    @Override
    public UserDto addUser(UserRequest userRequest) {
        Set<Role> roles = new HashSet<>();

        for(String s : userRequest.getRoles())
        {
            String s1 = "ROLE_"+s.toUpperCase();
            Role role = roleRepository.findByRoleName(s1);
            if(role == null)
                throw new ResourceNotFoundException("Role with name '"+s+"' not found");
            roles.add(new Role(s));
        }
        User user = new User();
        user.setUserName(userRequest.getUserName().toUpperCase());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRoles(roles);

        return userToDto(userRepository.save(user));

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
    public String restoreUserbyId(int id) {
        User user = userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User with id '"+id+"' not found!"));
        user.setDeleted(false);
        userRepository.save(user);
        return "User with id '"+id+"' restored!";
    }
}
