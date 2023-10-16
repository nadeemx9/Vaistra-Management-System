package com.vaistra.utils;

import com.vaistra.dto.bank.BankBranchDto;
import com.vaistra.dto.bank.BankDto;
import com.vaistra.dto.mastermines.*;
import com.vaistra.entities.*;
import com.vaistra.entities.bank.Bank;
import com.vaistra.entities.bank.BankBranch;
import com.vaistra.entities.cscv.*;
import com.vaistra.dto.*;
import com.vaistra.dto.cscv.*;
import com.vaistra.entities.mastermines.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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


    //---------------------------------------------------SUB-DISTRICT UTILS-------------------------------------------------
    public SubDistrictDto subDistrictToDto(SubDistrict subDistrict) {
        return modelMapper.map(subDistrict, SubDistrictDto.class);
    }
    public List<SubDistrictDto> subDistrictsToDtos(List<SubDistrict> subDistricts) {
        java.lang.reflect.Type targetListType = new TypeToken<List<SubDistrictDto>>() {}.getType();
        return modelMapper.map(subDistricts, targetListType);
    }

    //---------------------------------------------------VILLAGE UTILS-------------------------------------------------

    public VillageDto villageToDto(Village village) {

        VillageDto villageDto = new VillageDto();
        villageDto.setVillageId(village.getVillageId());
        villageDto.setVillageName(village.getVillageName());
        villageDto.setSubDistrictId(village.getSubDistrict().getSubDistrictId());
        villageDto.setSubDistrictName(village.getSubDistrict().getSubDistrictName());
        villageDto.setDistrictName(village.getDistrict().getDistrictName());
        villageDto.setStateName(village.getState().getStateName());
        villageDto.setCountryName(village.getCountry().getCountryName());
        villageDto.setStatus(village.getStatus());

        return villageDto;
    }

    public List<VillageDto> villagesToDtos(List<Village> villages) {

        List<VillageDto> dtos = new ArrayList<>();

        for (Village v:villages)
        {
            dtos.add(new VillageDto(v.getVillageId(), v.getVillageName(), v.getSubDistrict().getSubDistrictId(), v.getSubDistrict().getSubDistrictName(),
                    v.getDistrict().getDistrictName(), v.getState().getStateName(), v.getCountry().getCountryName(),v.getStatus()));
        }

        return dtos;

    }


    //---------------------------------------------------MINERAL UTILS-------------------------------------------------


    public MineralDto mineralToDto(Mineral mineral) {
        return modelMapper.map(mineral, MineralDto.class);
    }
    public List<MineralDto> mineralsToDtos(List<Mineral> minerals) {
        java.lang.reflect.Type targetListType = new TypeToken<List<MineralDto>>() {}.getType();
        return modelMapper.map(minerals, targetListType);
    }

    //---------------------------------------------------ENTITY UTILS-------------------------------------------------

    public EntityDto entityToDto(EntityTbl entityTbl) {
        return modelMapper.map(entityTbl, EntityDto.class);
    }
    public List<EntityDto> entitiesToDtos(List<EntityTbl> entities) {
        java.lang.reflect.Type targetListType = new TypeToken<List<EntityDto>>() {}.getType();
        return modelMapper.map(entities, targetListType);
    }

    //---------------------------------------------------DESIGNATION UTILS-------------------------------------------------

    public DesignationDto designationToDto(Designation designation) {
        return modelMapper.map(designation, DesignationDto.class);
    }
    public List<DesignationDto> designationsToDto(List<Designation> designations) {
        java.lang.reflect.Type targetListType = new TypeToken<List<DesignationDto>>() {}.getType();
        return modelMapper.map(designations, targetListType);
    }

    //---------------------------------------------------EQUIPMENT UTILS-------------------------------------------------

    public EquipmentDto equipmentToDto(Equipment equipment) {
        return modelMapper.map(equipment, EquipmentDto.class);
    }
    public List<EquipmentDto> equipmentsToDtos(List<Equipment> equipments) {
        java.lang.reflect.Type targetListType = new TypeToken<List<EquipmentDto>>() {}.getType();
        return modelMapper.map(equipments, targetListType);
    }

    //---------------------------------------------------VEHICLE UTILS-------------------------------------------------

    public VehicleDto vehicleToDto(Vehicle vehicle) {
        return modelMapper.map(vehicle, VehicleDto.class);
    }
    public List<VehicleDto> vehiclesToDtos(List<Vehicle> vehicles) {
        java.lang.reflect.Type targetListType = new TypeToken<List<VehicleDto>>() {}.getType();
        return modelMapper.map(vehicles, targetListType);
    }

    //---------------------------------------------------BANK BRANCH UTILS-------------------------------------------------

    public BankBranchDto bankBranchToDto(BankBranch bankBranch) {
        return modelMapper.map(bankBranch, BankBranchDto.class);
    }
    public List<BankBranchDto> bankBranchesToDtos(List<BankBranch> bankBranches) {
        java.lang.reflect.Type targetListType = new TypeToken<List<BankBranchDto>>() {}.getType();
        return modelMapper.map(bankBranches, targetListType);
    }

    //---------------------------------------------------BANK UTILS-------------------------------------------------

    public BankDto bankToDto(Bank bank) {
        return modelMapper.map(bank, BankDto.class);
    }
    public List<BankDto> banksToDtos(List<Bank> banks) {
        java.lang.reflect.Type targetListType = new TypeToken<List<BankDto>>() {}.getType();
        return modelMapper.map(banks, targetListType);
    }

    //---------------------------------------------------IMAGE UTILS-------------------------------------------------


    public boolean isSupportedExtension(String ext){
        int i = ext.lastIndexOf(".");

        String extension = "";

        if(i != -1){
            extension = ext.substring(i + 1);
        }

        return extension != null && (
                extension.equals("png")
                        || extension.equals("jpg")
                        || extension.equals("jpeg")
                        || extension.equals("pdf"))
                || extension.equals("JPG")
                || extension.equals("JPEG")
                || extension.equals("PDF")
                || extension.equals("PNG");
    }
    public boolean isSupportedExtensionBatch(String ext){
        int i = ext.lastIndexOf(".");

        String extension = "";

        if(i != -1){
            extension = ext.substring(i + 1);
        }

        return extension != null && (
                extension.equals("csv"))
                || extension.equals("CSV");
    }

}
