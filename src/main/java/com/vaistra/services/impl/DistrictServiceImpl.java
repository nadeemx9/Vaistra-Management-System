package com.vaistra.services.impl;

import com.vaistra.entities.District;
import com.vaistra.entities.State;
import com.vaistra.exception.CustomNullPointerException;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.InactiveStatusException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.payloads.DistrictDto;
import com.vaistra.repositories.CountryRepository;
import com.vaistra.repositories.DistrictRepository;
import com.vaistra.repositories.StateRepository;
import com.vaistra.services.DistrictService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final DistrictRepository districtRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository, StateRepository stateRepository, CountryRepository countryRepository)
    {
        this.districtRepository = districtRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }


    //----------------------------------------------------SERVICE METHODS-----------------------------------------------
    @Override
    public DistrictDto addDistrict(DistrictDto districtDto) {

        // HANDLE IF DUPLICATE DISTRICT NAME
        if(districtRepository.findByDistrictName(districtDto.getDistrictName()) != null)
            throw new DuplicateEntryException("District with name '"+districtDto.getDistrictName()+"' already exist!");

        // HANDLE IF STATE IS NULL
        int stateId;
        try {
            stateId = districtDto.getState().getStateId();
        }
        catch (Exception ex)
        {
            throw new CustomNullPointerException("State Id should not be empty!");
        }

        // HANDLE IF STATE EXIST BY ID
        State state = stateRepository.findById(stateId)
                .orElseThrow(()->new ResourceNotFoundException("State with Id '"+stateId+" not found!"));

        //  IS STATE STATUS ACTIVE ?
        if(!state.isStatus())
            throw new InactiveStatusException("State with id '"+stateId+"' is not active!");


        districtDto.setDistrictName(districtDto.getDistrictName().toUpperCase());
        districtDto.setState(AppUtils.stateToDto(state));

        return AppUtils.districtToDto(districtRepository.save(AppUtils.dtoToDistrict(districtDto)));
    }

    @Override
    public DistrictDto getDistrictById(int id) {
        return AppUtils.districtToDto(districtRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!")));
    }

    @Override
    public List<DistrictDto> getAllDistricts(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<District> pageDistrict = districtRepository.findAll(pageable);
        return AppUtils.districtsToDtos(pageDistrict.getContent());
    }
    @Override
    public List<DistrictDto> getAllDistrictsByDeleted(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<District> pageDistrict = districtRepository.findAllByDeleted(false, pageable);
        return AppUtils.districtsToDtos(pageDistrict.getContent());
    }

    @Override
    public DistrictDto updateDistrict(DistrictDto districtDto, int id) {
        // HANDLE IF DISTRICT EXIST BY ID
        District district = districtRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!"));

        // HANDLE IF DUPLICATE DISTRICT NAME
        if(districtRepository.findByDistrictName(districtDto.getDistrictName()) != null)
            throw new DuplicateEntryException("District with name '"+districtDto.getDistrictName()+"' already exist!");

        // HANDLE IF STATE IS EMPTY
        int stateId;
        try {
            stateId = districtDto.getState().getStateId();
        }
        catch (Exception ex){
            throw new CustomNullPointerException("State id should not empty!");
        }

        // HANDLE IF STATE EXIST BY ID
        stateRepository.findById(stateId)
                .orElseThrow(()->new ResourceNotFoundException("State with id '"+stateId+"' not found!"));;


        district.setDistrictName(districtDto.getDistrictName().toUpperCase());

        return AppUtils.districtToDto(districtRepository.save(district));
    }

    @Override
    public String deleteDistrictById(int id) {
        districtRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!"));
        districtRepository.deleteById(id);
        return "District with id '"+id+"' deleted!";
    }

    @Override
    public String softDeleteDistrictById(int id) {
        District district = districtRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!"));
        district.setDeleted(true);
        districtRepository.save(district);
        return "District with id '"+id+"' soft deleted!";
    }

    @Override
    public String restoreDistrictById(int id) {
        District district = districtRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!"));
        district.setDeleted(false);
        districtRepository.save(district);
        return "District with id '"+id+"' restored!";
    }

    @Override
    public List<DistrictDto> getDistrictByStateId(int stateId) {

        stateRepository.findById(stateId).
                orElseThrow(()->new ResourceNotFoundException("State with id '"+stateId+"' not found!"));
        return AppUtils.districtsToDtos(districtRepository.findByState_StateId(stateId));
    }

    @Override
    public List<DistrictDto> getDistrictByCountryId(int countryId) {
        countryRepository.findById(countryId)
                .orElseThrow(()->new ResourceNotFoundException("Country with id '"+countryId+"' not found!"));
        return AppUtils.districtsToDtos(districtRepository.findByState_Country_CountryId(countryId));
    }
}
