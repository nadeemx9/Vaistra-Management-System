package com.vaistra.utils;

import com.vaistra.entities.*;
import com.vaistra.payloads.*;
import org.hibernate.annotations.Comment;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AppUtils {

    private final ModelMapper modelMapper;

    @Autowired
    public AppUtils(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

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




    //---------------------------------------------------USER UTILS-----------------------------------------------------
    public UserDto userToDto(User user)
    {
        return modelMapper.map(user, UserDto.class);
    }
    public User dtoToUser(UserDto userDto)
    {
        return modelMapper.map(userDto, User.class);
    }

    public List<UserDto> usersToDtos(List<User> users) {
        java.lang.reflect.Type targetListType = new TypeToken<List<UserDto>>() {}.getType();
        return modelMapper.map(users, targetListType);
    }


    //---------------------------------------------------COUNTRY UTILS--------------------------------------------------
    public CountryDto countryToDto(Country country) {
        return modelMapper.map(country, CountryDto.class);
    }

    public Country dtoToCountry(CountryDto dto) {
        return modelMapper.map(dto, Country.class);    }

    public List<CountryDto> countriesToDtos(List<Country> countries) {
        java.lang.reflect.Type targetListType = new TypeToken<List<CountryDto>>() {}.getType();
        return modelMapper.map(countries, targetListType);
    }

    public List<Country> dtosToCountries(List<CountryDto> dtos) {
        java.lang.reflect.Type targetListType = new TypeToken<List<Country>>() {}.getType();
        return modelMapper.map(dtos, targetListType);
    }


    //---------------------------------------------------STATE UTILS----------------------------------------------------
    public StateDto stateToDto(State state) {
        return modelMapper.map(state, StateDto.class);
    }

    public State dtoToState(StateDto dto) {
        return modelMapper.map(dto, State.class);    }

    public List<StateDto> statesToDtos(List<State> states) {
        java.lang.reflect.Type targetListType = new TypeToken<List<StateDto>>() {}.getType();
        return modelMapper.map(states, targetListType);
    }

    public List<State> dtosToStates(List<StateDto> dtos) {
        java.lang.reflect.Type targetListType = new TypeToken<List<State>>() {}.getType();
        return modelMapper.map(dtos, targetListType);
    }


    //---------------------------------------------------DISTRICT UTILS-------------------------------------------------

    public DistrictDto districtToDto(District district) {
        return modelMapper.map(district, DistrictDto.class);
    }

    public District dtoToDistrict(DistrictDto dto) {
        return modelMapper.map(dto, District.class);
    }

    public List<DistrictDto> districtsToDtos(List<District> districts) {
        java.lang.reflect.Type targetListType = new TypeToken<List<DistrictDto>>() {}.getType();
        return modelMapper.map(districts, targetListType);
    }

}
