package com.vaistra.services.cscv.impl;

import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.State;
import com.vaistra.entities.cscv.SubDistrict;
import com.vaistra.entities.cscv.Village;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.InactiveStatusException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.cscv.VillageDto;
import com.vaistra.repositories.cscv.*;
import com.vaistra.services.cscv.VillageService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VillageServiceImpl implements VillageService {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final DistrictRepository districtRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final AppUtils appUtils;
    private final SubDistrictRepository subDistrictRepository;
    private final VillageRepository villageRepository;

    @Autowired
    public VillageServiceImpl(DistrictRepository districtRepository, StateRepository stateRepository, CountryRepository countryRepository, AppUtils appUtils, SubDistrictRepository subDistrictRepository,
                              VillageRepository villageRepository) {
        this.districtRepository = districtRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
        this.appUtils = appUtils;
        this.subDistrictRepository = subDistrictRepository;
        this.villageRepository = villageRepository;
    }

    @Override
    public VillageDto addVillage(VillageDto villageDto) {

        villageDto.setVillageName(villageDto.getVillageName().trim().toUpperCase());

        // HANDLE IF SUB-DISTRICT EXIST BY ID
        SubDistrict subDistrict = subDistrictRepository.findById(villageDto.getSubDistrictId())
                .orElseThrow(()->new ResourceNotFoundException("Sub-District with ID '"+villageDto.getSubDistrictId()+"' not found!"));

        // HANDLE IF DUPLICATE VILLAGE NAME
        if(villageRepository.existsByVillageName(villageDto.getVillageName()))
            throw new DuplicateEntryException("Village with name '"+villageDto.getVillageName()+"' already exist!");

//        IS SUB-DISTRICT STATUS ACTIVE
        if(!subDistrict.getStatus())
            throw new InactiveStatusException("Sub-District '"+subDistrict.getSubDistrictName()+"' is inactive!");

        Village village = new Village();
        village.setVillageName(villageDto.getVillageName());
        village.setCountry(subDistrict.getCountry());
        village.setState(subDistrict.getState());
        village.setDistrict(subDistrict.getDistrict());
        village.setSubDistrict(subDistrict);
        if(villageDto.getStatus() != null)
            village.setStatus(villageDto.getStatus());
        else
            village.setStatus(true);

        return appUtils.villageToDto(villageRepository.save(village));
    }

    @Override
    public VillageDto getVillageById(int id) {
        return appUtils.villageToDto(villageRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Village with ID '"+id+"' not found!")));
    }

    @Override
    public HttpResponse getAllVillages(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Village> pageVillage = villageRepository.findAll(pageable);
        List<VillageDto> villages = appUtils.villagesToDtos(pageVillage.getContent());

        return HttpResponse.builder()
                .pageNumber(pageVillage.getNumber())
                .pageSize(pageVillage.getSize())
                .totalElements(pageVillage.getTotalElements())
                .totalPages(pageVillage.getTotalPages())
                .isLastPage(pageVillage.isLast())
                .data(villages)
                .build();
    }

    @Override
    public HttpResponse getAllVillagesByActiveSubDistricts(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Village> pageVillage = villageRepository.findAllBySubDistrict_Status(true, pageable);
        List<VillageDto> villages = appUtils.villagesToDtos(pageVillage.getContent());

        return HttpResponse.builder()
                .pageNumber(pageVillage.getNumber())
                .pageSize(pageVillage.getSize())
                .totalElements(pageVillage.getTotalElements())
                .totalPages(pageVillage.getTotalPages())
                .isLastPage(pageVillage.isLast())
                .data(villages)
                .build();
    }

    @Override
    public HttpResponse getAllVillagesBySubDistrict(int subDistrictId, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        SubDistrict subDistrict = subDistrictRepository.findById(subDistrictId)
                .orElseThrow(()->new ResourceNotFoundException("Sub-District with ID '"+subDistrictId+"' not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Village> pageVillage = villageRepository.findAllBySubDistrict(subDistrict, pageable);
        List<VillageDto> villages = appUtils.villagesToDtos(pageVillage.getContent());

        return HttpResponse.builder()
                .pageNumber(pageVillage.getNumber())
                .pageSize(pageVillage.getSize())
                .totalElements(pageVillage.getTotalElements())
                .totalPages(pageVillage.getTotalPages())
                .isLastPage(pageVillage.isLast())
                .data(villages)
                .build();
    }

    @Override
    public HttpResponse getAlLVillagesByState(int stateId, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        State state = stateRepository.findById(stateId)
                .orElseThrow(()->new ResourceNotFoundException("State with ID '"+stateId+"' not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Village> pageVillage = villageRepository.findAllBySubDistrict_District_State(state, pageable);
        List<VillageDto> villages = appUtils.villagesToDtos(pageVillage.getContent());

        return HttpResponse.builder()
                .pageNumber(pageVillage.getNumber())
                .pageSize(pageVillage.getSize())
                .totalElements(pageVillage.getTotalElements())
                .totalPages(pageVillage.getTotalPages())
                .isLastPage(pageVillage.isLast())
                .data(villages)
                .build();
    }


    @Override
    public HttpResponse getAllVillagesByCountry(int countryId, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(()->new ResourceNotFoundException("State with ID '"+countryId+"' not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Village> pageVillage = villageRepository.findAllBySubDistrict_District_State_Country(country, pageable);
        List<VillageDto> villages = appUtils.villagesToDtos(pageVillage.getContent());

        return HttpResponse.builder()
                .pageNumber(pageVillage.getNumber())
                .pageSize(pageVillage.getSize())
                .totalElements(pageVillage.getTotalElements())
                .totalPages(pageVillage.getTotalPages())
                .isLastPage(pageVillage.isLast())
                .data(villages)
                .build();
    }

    @Override
    public HttpResponse searchVillagesByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Integer integerKeyword = null;
        Boolean booleanKeyword = null;

        if(keyword.equalsIgnoreCase("true"))
            booleanKeyword = Boolean.TRUE;
        else if (keyword.equalsIgnoreCase("false"))
            booleanKeyword = Boolean.FALSE;

        try {
            integerKeyword = Integer.parseInt(keyword);
        }catch (Exception e){

        }

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Village> pageVillage = villageRepository.findAllByVillageIdOrStatusOrVillageNameContainingIgnoreCaseOrSubDistrict_SubDistrictNameContainingIgnoreCaseOrDistrict_DistrictNameContainingIgnoreCaseOrState_StateNameContainingIgnoreCaseOrCountry_CountryNameContainingIgnoreCase
                (integerKeyword, booleanKeyword, keyword, keyword, keyword, keyword, keyword, pageable);
        List<VillageDto> villages = appUtils.villagesToDtos(pageVillage.getContent());

        return HttpResponse.builder()
                .pageNumber(pageVillage.getNumber())
                .pageSize(pageVillage.getSize())
                .totalElements(pageVillage.getTotalElements())
                .totalPages(pageVillage.getTotalPages())
                .isLastPage(pageVillage.isLast())
                .data(villages)
                .build();
    }

    @Override
    public VillageDto updateVillage(VillageDto villageDto, int villageId) {

        villageDto.setVillageName(villageDto.getVillageName().trim().toUpperCase());

        // HANDLE IF VILLAGE EXIST BY ID
        Village village = villageRepository.findById(villageDto.getSubDistrictId())
                .orElseThrow(()->new ResourceNotFoundException("Village with ID '"+villageId+"' not found!"));

        // HANDLE IF DUPLICATE VILLAGE NAME
        if(villageRepository.existsByVillageName(villageDto.getVillageName()))
            throw new DuplicateEntryException("Village with name '"+villageDto.getVillageName()+"' already exist!");

        village.setVillageName(villageDto.getVillageName());

        return appUtils.villageToDto(villageRepository.save(village));

    }

    @Override
    public String deleteVillageById(int id) {
        Village village = villageRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Village with ID '"+id+"' not found!"));

        villageRepository.delete(village);
        return "Village '"+village.getVillageName()+"' deleted!";
    }
}
