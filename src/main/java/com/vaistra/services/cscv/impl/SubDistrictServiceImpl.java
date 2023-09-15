package com.vaistra.services.cscv.impl;

import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.District;
import com.vaistra.entities.cscv.State;
import com.vaistra.entities.cscv.SubDistrict;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.InactiveStatusException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.cscv.SubDistrictDto;
import com.vaistra.repositories.cscv.CountryRepository;
import com.vaistra.repositories.cscv.DistrictRepository;
import com.vaistra.repositories.cscv.StateRepository;
import com.vaistra.repositories.cscv.SubDistrictRepository;
import com.vaistra.services.cscv.SubDistrictService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubDistrictServiceImpl implements SubDistrictService {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final DistrictRepository districtRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final AppUtils appUtils;
    private final SubDistrictRepository subDistrictRepository;

    @Autowired
    public SubDistrictServiceImpl(DistrictRepository districtRepository, StateRepository stateRepository, CountryRepository countryRepository, AppUtils appUtils,
                                  SubDistrictRepository subDistrictRepository) {
        this.districtRepository = districtRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
        this.appUtils = appUtils;
        this.subDistrictRepository = subDistrictRepository;
    }


    @Override
    public SubDistrictDto addSubDistrict(SubDistrictDto subDistrictDto) {

        subDistrictDto.setSubDistrictName(subDistrictDto.getSubDistrictName().trim().toUpperCase());

        // HANDLE IF DUPLICATE SUB-DISTRICT NAME
        if(subDistrictRepository.existsBySubDistrictName(subDistrictDto.getSubDistrictName()))
            throw new DuplicateEntryException("Sub-District with name '"+subDistrictDto.getSubDistrictName()+"' already exist!");

        // HANDLE IF DISTRICT EXIST BY ID
        District district = districtRepository.findById(subDistrictDto.getDistrictId())
                .orElseThrow(()->new ResourceNotFoundException("District with ID '"+subDistrictDto.getDistrictId()+"' not found!"));

        //  IS DISTRICT STATUS ACTIVE ?
        if(!district.getStatus())
            throw new InactiveStatusException("District '"+district.getDistrictName()+"' is inactive!");

        SubDistrict subDistrict = new SubDistrict();
        subDistrict.setSubDistrictName(subDistrictDto.getSubDistrictName());
        subDistrict.setCountry(district.getCountry());
        subDistrict.setState(district.getState());
        subDistrict.setDistrict(district);
        if(subDistrictDto.getStatus() != null)
            subDistrict.setStatus(subDistrictDto.getStatus());
        else
            subDistrict.setStatus(true);

        return appUtils.subDistrictToDto(subDistrictRepository.save(subDistrict));
    }

    @Override
    public SubDistrictDto getSubDistrictById(int id) {
        return appUtils.subDistrictToDto(subDistrictRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Sub-District with ID '"+id+"' not found!")));
    }

    @Override
    public HttpResponse getAllSubDistricts(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<SubDistrict> pageSubDistrict = subDistrictRepository.findAll(pageable);
        List<SubDistrictDto> subDistricts = appUtils.subDistrictsToDtos(pageSubDistrict.getContent());

        return HttpResponse.builder()
                .pageNumber(pageSubDistrict.getNumber())
                .pageSize(pageSubDistrict.getSize())
                .totalElements(pageSubDistrict.getTotalElements())
                .totalPages(pageSubDistrict.getTotalPages())
                .isLastPage(pageSubDistrict.isLast())
                .data(subDistricts)
                .build();
    }

    @Override
    public HttpResponse getAllSubDistrictsByActiveDistrict(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<SubDistrict> pageSubDistrict = subDistrictRepository.findAllByDistrict_Status(true, pageable);
        List<SubDistrictDto> subDistricts = appUtils.subDistrictsToDtos(pageSubDistrict.getContent());

        return HttpResponse.builder()
                .pageNumber(pageSubDistrict.getNumber())
                .pageSize(pageSubDistrict.getSize())
                .totalElements(pageSubDistrict.getTotalElements())
                .totalPages(pageSubDistrict.getTotalPages())
                .isLastPage(pageSubDistrict.isLast())
                .data(subDistricts)
                .build();
    }


    @Override
    public HttpResponse getAllSubDistrictsByDistrict(int districtId, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        District district = districtRepository.findById(districtId)
                .orElseThrow(()->new ResourceNotFoundException("District with ID '"+districtId+"' not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<SubDistrict> pageSubDistrict = subDistrictRepository.findAllByDistrict(district, pageable);
        List<SubDistrictDto> subDistricts = appUtils.subDistrictsToDtos(pageSubDistrict.getContent());

        return HttpResponse.builder()
                .pageNumber(pageSubDistrict.getNumber())
                .pageSize(pageSubDistrict.getSize())
                .totalElements(pageSubDistrict.getTotalElements())
                .totalPages(pageSubDistrict.getTotalPages())
                .isLastPage(pageSubDistrict.isLast())
                .data(subDistricts)
                .build();
    }

    @Override
    public HttpResponse getAlLSubDistrictsByState(int stateId, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        State state = stateRepository.findById(stateId)
                .orElseThrow(()->new ResourceNotFoundException("State with ID '"+stateId+"' not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<SubDistrict> pageSubDistrict = subDistrictRepository.findAllByDistrict_State(state, pageable);
        List<SubDistrictDto> subDistricts = appUtils.subDistrictsToDtos(pageSubDistrict.getContent());

        return HttpResponse.builder()
                .pageNumber(pageSubDistrict.getNumber())
                .pageSize(pageSubDistrict.getSize())
                .totalElements(pageSubDistrict.getTotalElements())
                .totalPages(pageSubDistrict.getTotalPages())
                .isLastPage(pageSubDistrict.isLast())
                .data(subDistricts)
                .build();
    }


    @Override
    public HttpResponse getAllSubDistrictsByCountry(int countryId, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Country country = countryRepository.findById(countryId)
                .orElseThrow(()->new ResourceNotFoundException("Country with ID '"+countryId+"' not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<SubDistrict> pageSubDistrict = subDistrictRepository.findAllByDistrict_State_Country(country, pageable);
        List<SubDistrictDto> subDistricts = appUtils.subDistrictsToDtos(pageSubDistrict.getContent());

        return HttpResponse.builder()
                .pageNumber(pageSubDistrict.getNumber())
                .pageSize(pageSubDistrict.getSize())
                .totalElements(pageSubDistrict.getTotalElements())
                .totalPages(pageSubDistrict.getTotalPages())
                .isLastPage(pageSubDistrict.isLast())
                .data(subDistricts)
                .build();
    }

    @Override
    public HttpResponse searchSubDistrictByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {

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
        Page<SubDistrict> pageSubDistrict = subDistrictRepository
                .findAllBySubDistrictIdOrStatusOrSubDistrictNameContainingIgnoreCaseOrDistrict_DistrictNameContainingIgnoreCaseOrState_StateNameContainingIgnoreCaseOrCountry_CountryNameContainingIgnoreCase
                        (integerKeyword, booleanKeyword, keyword, keyword, keyword, keyword, pageable);
        List<SubDistrictDto> subDistricts = appUtils.subDistrictsToDtos(pageSubDistrict.getContent());

        return HttpResponse.builder()
                .pageNumber(pageSubDistrict.getNumber())
                .pageSize(pageSubDistrict.getSize())
                .totalElements(pageSubDistrict.getTotalElements())
                .totalPages(pageSubDistrict.getTotalPages())
                .isLastPage(pageSubDistrict.isLast())
                .data(subDistricts)
                .build();
    }

    @Override
    public SubDistrictDto updateSubDistrict(SubDistrictDto subDistrictDto, int id) {

        // HANDLE IF SUB-DISTRICT EXIST BY ID
        SubDistrict subDistrict = subDistrictRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Sub-District with ID '"+id+"' not found!"));

        // HANDLE IF DUPLICATE SUB-DISTRICT NAME
        if(subDistrictDto.getSubDistrictName() != null)
        {
            SubDistrict subDistrictWithSameName = subDistrictRepository.findBySubDistrictNameIgnoreCase(subDistrictDto.getSubDistrictName().trim());
            if(subDistrictWithSameName != null && !subDistrictWithSameName.getSubDistrictId().equals(subDistrict.getSubDistrictId()))
                throw new DuplicateEntryException("Sub-District '"+subDistrictDto.getSubDistrictName()+"' already exist!");

            subDistrict.setSubDistrictName(subDistrictDto.getSubDistrictName().trim().toUpperCase());
        }

        if(subDistrictDto.getDistrictId() != null)
        {
            District district = districtRepository.findById(subDistrictDto.getDistrictId())
                    .orElseThrow(()->new ResourceNotFoundException("District with ID '"+subDistrictDto.getDistrictId()+"' not found!"));
            State state = stateRepository.findById(district.getState().getStateId())
                    .orElseThrow(()->new ResourceNotFoundException("State with ID '"+district.getState().getStateId()+"' not found!"));
            Country country = countryRepository.findById(state.getCountry().getCountryId())
                    .orElseThrow(()->new ResourceNotFoundException("Country with ID '"+state.getCountry().getCountryId()+"' not found!"));

            subDistrict.setDistrict(district);
            subDistrict.setState(state);
            subDistrict.setCountry(country);
        }

        if(subDistrictDto.getStatus() != null)
            subDistrict.setStatus(subDistrictDto.getStatus());

        return appUtils.subDistrictToDto(subDistrictRepository.save(subDistrict));
    }

    @Override
    public String deleteSubDistrictById(int id) {
        SubDistrict subDistrict = subDistrictRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Sub-District with ID '"+id+"' not found!"));

        subDistrictRepository.delete(subDistrict);
        return "Sub-District deleted!";
    }
}
