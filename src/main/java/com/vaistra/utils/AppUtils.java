package com.vaistra.utils;

import com.vaistra.entities.*;
import com.vaistra.payloads.*;
import com.vaistra.services.impl.CountryServiceImpl;
import com.vaistra.services.impl.RoleServiceImpl;
import com.vaistra.services.impl.StateServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppUtils {

    //---------------------------------------------------EMAIL UTILS----------------------------------------------------

    public static String getEmailMessage(String name, String host, String token)
    {
        return "Hello, "+name+",\n\n Your new account has been created. Please click the link below to verify your account. \n\n"
                +getVerificationURL(host, token)+"\n\n Team VaistraTech.";
    }

    public static String getVerificationURL(String host, String token)
    {
        return host+"/user/verify?token="+token;
    }



    //---------------------------------------------------ROLE UTILS----------------------------------------------------
    public static RoleDto roleToDto(Role role)
    {
        return new RoleDto(role.getRoleId(), role.getRoleName());
    }
    public static Role dtoToRole(RoleDto roleDto)
    {
        return new Role(roleDto.getRoleId(), roleDto.getRoleName());
    }


    public static Set<RoleDto> setOfRolesToSetOfRolesDto(Set<Role> roles)
    {
        Set<RoleDto> roleDtos = new HashSet<>();
        for(Role role : roles)
        {
            roleDtos.add(new RoleDto(role.getRoleId(),role.getRoleName()));
        }
        return roleDtos;
    }
    public static Set<Role> setOfRolesDtoToSetOfRoles(Set<RoleDto> dtos)
    {
        Set<Role> roles = new HashSet<>();
        for(RoleDto dto : dtos)
        {
            roles.add(new Role(dto.getRoleId(),dto.getRoleName()));
        }
        return roles;
    }

    public static List<RoleDto> rolesToDtos(List<Role> roles)
    {
        List<RoleDto> dto = new ArrayList<>();
        for (Role r : roles)
        {
            dto.add(new RoleDto(r.getRoleId(), r.getRoleName()));
        }
        return dto;
    }


    //---------------------------------------------------USER UTILS-----------------------------------------------------
    public static UserDto userToDto(User user) {
        return new UserDto(user.getUserId(), user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPhoneNumber(), user.getGender(), user.getAddress(), user.isStatus(), user.getCreatedAt(),
                user.isDeleted(), user.getLastLogin(), user.getLastAccessIp(), setOfRolesToSetOfRolesDto(user.getRoles()));


    }

    public static User dtoToUser(UserDto dto) {
        return new User(dto.getUserId(), dto.getUserName(), dto.getPassword(), dto.getFirstName(), dto.getLastName(), dto.getEmail(),
                dto.getPhoneNumber(), dto.getGender(), dto.getAddress(), dto.isStatus(), dto.getCreatedAt(), dto.isDeleted(), dto.getLastLogin(),
                dto.getLastAccessIp(), setOfRolesDtoToSetOfRoles(dto.getRoles()));
    }

    public static List<UserDto> usersToDtos(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for (User u : users) {
            dtos.add(new UserDto(u.getUserId(), u.getUsername(), u.getPassword(), u.getFirstName(), u.getLastName(), u.getEmail(),
                    u.getPhoneNumber(), u.getGender(), u.getAddress(), u.isStatus(), u.getCreatedAt(), u.isDeleted(), u.getLastLogin(),
                    u.getLastAccessIp(), setOfRolesToSetOfRolesDto(u.getRoles())));
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
                    u.getLastAccessIp(), setOfRolesToSetOfRolesDto(u.getRoles())));
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
                    d.getLastAccessIp(), setOfRolesDtoToSetOfRoles(d.getRoles())));
        }
        return users;
    }



    //---------------------------------------------------COUNTRY UTILS--------------------------------------------------
    public static CountryDto countryToDto(Country country) {
        return new CountryDto(country.getCountryId(), country.getCountryName(), country.isStatus(), country.isDeleted());
    }

    public static Country dtoToCountry(CountryDto dto) {
        return new Country(dto.getCountryId(), dto.getCountryName(), dto.isStatus(), dto.isDeleted());
    }

    public static List<CountryDto> countriesToDtos(List<Country> countries) {
        List<CountryDto> dtos = new ArrayList<>();
        for (Country c : countries) {
            dtos.add(new CountryDto(c.getCountryId(), c.getCountryName(), c.isStatus(), c.isDeleted()));
        }
        return dtos;
    }

    public static List<Country> dtosToCountries(List<CountryDto> dtos) {
        List<Country> countries = new ArrayList<>();
        for (CountryDto dto : dtos) {
            countries.add(new Country(dto.getCountryId(), dto.getCountryName(), dto.isStatus(), dto.isDeleted()));
        }
        return countries;
    }


    //---------------------------------------------------STATE UTILS----------------------------------------------------
    public static StateDto stateToDto(State state) {
        return new StateDto(state.getStateId(), state.getStateName(), state.isStatus(), state.isDeleted(), countryToDto(state.getCountry()));
    }

    public static State dtoToState(StateDto dto) {
        return new State(dto.getStateId(), dto.getStateName(), dto.isStatus(), dto.isDeleted(), dtoToCountry(dto.getCountry()));
    }

    public static List<StateDto> statesToDtos(List<State> states) {
        List<StateDto> dtos = new ArrayList<>();

        for (State s : states)
            dtos.add(new StateDto(s.getStateId(), s.getStateName(), s.isStatus(), s.isDeleted(), countryToDto(s.getCountry())));

        return dtos;
    }

    public static List<State> dtosToStates(List<StateDto> dtos) {
        List<State> states = new ArrayList<>();
        for (StateDto d : dtos)
            states.add(new State(d.getStateId(), d.getStateName(), d.isStatus(), d.isDeleted(), dtoToCountry(d.getCountry())));

        return states;
    }


    //---------------------------------------------------DISTRICT UTILS-------------------------------------------------

    public static DistrictDto districtToDto(District district) {
        return new DistrictDto(district.getDistrictId(), district.getDistrictName(), district.isStatus(), district.isDeleted(), stateToDto(district.getState()));
    }

    public static District dtoToDistrict(DistrictDto dto) {
        return new District(dto.getDistrictId(), dto.getDistrictName(), dto.isStatus(), dto.isDeleted(), dtoToState(dto.getState()));
    }

    public static List<DistrictDto> districtsToDtos(List<District> districts) {
        List<DistrictDto> dtos = new ArrayList<>();

        for (District d : districts)
            dtos.add(new DistrictDto(d.getDistrictId(), d.getDistrictName(), d.isStatus(), d.isDeleted(), stateToDto(d.getState())));

        return dtos;
    }



}
