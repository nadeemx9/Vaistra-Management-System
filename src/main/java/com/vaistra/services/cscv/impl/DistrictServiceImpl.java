package com.vaistra.services.cscv.impl;

import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.District;
import com.vaistra.entities.cscv.State;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.InactiveStatusException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.dto.cscv.DistrictDto;
import com.vaistra.dto.HttpResponse;
import com.vaistra.repositories.cscv.CountryRepository;
import com.vaistra.repositories.cscv.DistrictRepository;
import com.vaistra.repositories.cscv.StateRepository;
import com.vaistra.services.cscv.DistrictService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final DistrictRepository districtRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final AppUtils appUtils;


    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository, StateRepository stateRepository,
                               CountryRepository countryRepository, AppUtils appUtils)
    {
        this.districtRepository = districtRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
        this.appUtils = appUtils;
    }


    //----------------------------------------------------SERVICE METHODS-----------------------------------------------
    @Override
    public DistrictDto addDistrict(DistrictDto districtDto) {

        districtDto.setDistrictName(districtDto.getDistrictName().trim().toUpperCase());

        // HANDLE IF DUPLICATE DISTRICT NAME
        if(districtRepository.existsByDistrictNameIgnoreCase(districtDto.getDistrictName()))
            throw new DuplicateEntryException("District with name '"+districtDto.getDistrictName()+"' already exist!");

        // HANDLE IF STATE EXIST BY ID
        State state = stateRepository.findById(districtDto.getStateId())
                .orElseThrow(()->new ResourceNotFoundException("State with Id '"+districtDto.getStateId()+" not found!"));

        //  IS STATE STATUS ACTIVE ?
        if(!state.getStatus())
            throw new InactiveStatusException("State with id '"+districtDto.getStateId()+"' is not active!");


        District district = new District();
        district.setDistrictName(districtDto.getDistrictName());
        district.setCountry(state.getCountry());
        district.setState(state);
        if(districtDto.getStatus() != null)
            district.setStatus(districtDto.getStatus());
        else
            district.setStatus(true);

        return appUtils.districtToDto(districtRepository.save(district));
    }

    @Override
    public DistrictDto getDistrictById(int id) {
        return appUtils.districtToDto(districtRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!")));
    }

    @Override
    public HttpResponse getAllDistricts(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<District> pageDistrict = districtRepository.findAll(pageable);
        List<DistrictDto> districts = appUtils.districtsToDtos(pageDistrict.getContent());

        return HttpResponse.builder()
                .pageNumber(pageDistrict.getNumber())
                .pageSize(pageDistrict.getSize())
                .totalElements(pageDistrict.getTotalElements())
                .totalPages(pageDistrict.getTotalPages())
                .isLastPage(pageDistrict.isLast())
                .data(districts)
                .build();
    }
    @Override
    public HttpResponse getAllDistrictsByActiveState(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<District> pageDistrict = districtRepository.findAllByState_Status(true, pageable);
        List<DistrictDto> districts = appUtils.districtsToDtos(pageDistrict.getContent());

        return HttpResponse.builder()
                .pageNumber(pageDistrict.getNumber())
                .pageSize(pageDistrict.getSize())
                .totalElements(pageDistrict.getTotalElements())
                .totalPages(pageDistrict.getTotalPages())
                .isLastPage(pageDistrict.isLast())
                .data(districts)
                .build();
    }
    @Override
    public HttpResponse getDistrictsByStateId(int stateId, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        State state = stateRepository.findById(stateId)
                .orElseThrow(()->new ResourceNotFoundException("State with ID '"+stateId+"' not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<District> pageDistrict = districtRepository.findAllByState(state, pageable);
        List<DistrictDto> districts = appUtils.districtsToDtos(pageDistrict.getContent());

        return HttpResponse.builder()
                .pageNumber(pageDistrict.getNumber())
                .pageSize(pageDistrict.getSize())
                .totalElements(pageDistrict.getTotalElements())
                .totalPages(pageDistrict.getTotalPages())
                .isLastPage(pageDistrict.isLast())
                .data(districts)
                .build();
    }

    @Override
    public HttpResponse getDistrictsByCountryId(int countryId, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(()->new ResourceNotFoundException("Country with id '"+countryId+"' not found!"));


        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<District> pageDistrict = districtRepository.findAllByState_Country(country, pageable);
        List<DistrictDto> districts = appUtils.districtsToDtos(pageDistrict.getContent());

        return HttpResponse.builder()
                .pageNumber(pageDistrict.getNumber())
                .pageSize(pageDistrict.getSize())
                .totalElements(pageDistrict.getTotalElements())
                .totalPages(pageDistrict.getTotalPages())
                .isLastPage(pageDistrict.isLast())
                .data(districts)
                .build();
    }

    @Override
    public HttpResponse searchDistrictByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {

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
        Page<District> pageDistrict = districtRepository
                .findAllByDistrictIdOrStatusOrDistrictNameContainingIgnoreCaseOrState_StateNameContainingIgnoreCaseOrCountry_CountryNameContainingIgnoreCase
                        (integerKeyword, booleanKeyword, keyword, keyword, keyword, pageable);

        List<DistrictDto> districts = appUtils.districtsToDtos(pageDistrict.getContent());

        return HttpResponse.builder()
                .pageNumber(pageDistrict.getNumber())
                .pageSize(pageDistrict.getSize())
                .totalElements(pageDistrict.getTotalElements())
                .totalPages(pageDistrict.getTotalPages())
                .isLastPage(pageDistrict.isLast())
                .data(districts)
                .build();
    }

    @Override
    public DistrictDto updateDistrict(DistrictDto districtDto, int id) {

        // HANDLE IF DISTRICT EXIST BY ID
        District district = districtRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!"));

        // HANDLE IF DUPLICATE DISTRICT NAME
        if(districtDto.getDistrictName() != null)
        {
            District districtWithSameName = districtRepository.findByDistrictNameIgnoreCase(districtDto.getDistrictName().trim());

            if(districtWithSameName != null && !districtWithSameName.getDistrictId().equals(district.getDistrictId()))
                throw new DuplicateEntryException("District '"+districtDto.getDistrictName()+"' already exist!");

            district.setDistrictName(districtDto.getDistrictName().trim().toUpperCase());
        }

        if(districtDto.getStateId() != null)
        {
            State state = stateRepository.findById(districtDto.getStateId())
                    .orElseThrow(()->new ResourceNotFoundException("State with ID '"+districtDto.getStateId()+"' not found!"));
            Country country = countryRepository.findById(state.getCountry().getCountryId())
                    .orElseThrow(()->new ResourceNotFoundException("Country with ID '"+state.getCountry().getCountryId()+"' not found!"));

            district.setState(state);
            district.setCountry(country);
        }

        if(districtDto.getStatus() != null)
            district.setStatus(districtDto.getStatus());

        return appUtils.districtToDto(districtRepository.save(district));
    }

    @Override
    public String deleteDistrictById(int id) {
        districtRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!"));

        districtRepository.deleteById(id);
        return "District with id '"+id+"' deleted!";
    }

//    @Override
//    public String softDeleteDistrictById(int id) {
//        District district = districtRepository.findById(id)
//                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!"));
//        district.setDeleted(true);
//        districtRepository.save(district);
//        return "District with id '"+id+"' soft deleted!";
//    }
//
//    @Override
//    public String restoreDistrictById(int id) {
//        District district = districtRepository.findById(id)
//                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!"));
//        district.setDeleted(false);
//        districtRepository.save(district);
//        return "District with id '"+id+"' restored!";
//    }


}
