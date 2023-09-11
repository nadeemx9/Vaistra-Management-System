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
        if(districtRepository.existsByDistrictName(districtDto.getDistrictName()))
            throw new DuplicateEntryException("District with name '"+districtDto.getDistrictName()+"' already exist!");

        // HANDLE IF STATE EXIST BY ID
        State state = stateRepository.findById(districtDto.getStateId())
                .orElseThrow(()->new ResourceNotFoundException("State with Id '"+districtDto.getStateId()+" not found!"));

        //  IS STATE STATUS ACTIVE ?
        if(!state.isStatus())
            throw new InactiveStatusException("State with id '"+districtDto.getStateId()+"' is not active!");


        District district = new District();
        district.setDistrictName(districtDto.getDistrictName());
        district.setState(state);
        district.setStatus(true);

        return appUtils.districtToDto(districtRepository.save(district));
    }

    @Override
    public DistrictDto getDistrictById(int id) {
        return appUtils.districtToDto(districtRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!")));
    }

    @Override
    public List<DistrictDto> getAllDistricts(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<District> pageDistrict = districtRepository.findAll(pageable);
        return appUtils.districtsToDtos(pageDistrict.getContent());
    }
    @Override
    public List<DistrictDto> getAllDistrictsByActive(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<District> pageDistrict = districtRepository.findAllByStatus(true, pageable);
        return appUtils.districtsToDtos(pageDistrict.getContent());
    }

    @Override
    public DistrictDto updateDistrict(DistrictDto districtDto, int id) {

        districtDto.setDistrictName(districtDto.getDistrictName().trim().toUpperCase());
        // HANDLE IF DISTRICT EXIST BY ID
        District district = districtRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("District with id '"+id+"' not found!"));

        // HANDLE IF DUPLICATE DISTRICT NAME
        if(districtRepository.existsByDistrictName(districtDto.getDistrictName()))
            throw new DuplicateEntryException("District with name '"+districtDto.getDistrictName()+"' already exist!");

        district.setDistrictName(districtDto.getDistrictName());

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

    @Override
    public List<DistrictDto> getDistrictsByStateId(int stateId) {

        stateRepository.findById(stateId).
                orElseThrow(()->new ResourceNotFoundException("State with id '"+stateId+"' not found!"));
        return appUtils.districtsToDtos(districtRepository.findByState_StateId(stateId));
    }

    @Override
    public List<DistrictDto> getDistrictsByCountryId(int countryId) {
        countryRepository.findById(countryId)
                .orElseThrow(()->new ResourceNotFoundException("Country with id '"+countryId+"' not found!"));
        return appUtils.districtsToDtos(districtRepository.findByState_Country_CountryId(countryId));
    }
}
