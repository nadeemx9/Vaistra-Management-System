package com.vaistra.services.impl;

import com.vaistra.entities.Country;
import com.vaistra.entities.State;
import com.vaistra.exception.CustomNullPointerException;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.InactiveStatusException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.payloads.StateDto;
import com.vaistra.repositories.CountryRepository;
import com.vaistra.repositories.StateRepository;
import com.vaistra.services.StateService;
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
public class StateServiceImpl implements StateService {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final AppUtils appUtils;


    @Autowired
    public StateServiceImpl(StateRepository stateRepository, CountryRepository countryRepository, AppUtils appUtils) {
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
        this.appUtils = appUtils;
    }




    //----------------------------------------------------SERVICE METHODS-----------------------------------------------
    @Override
    public StateDto addState(StateDto stateDto) {

        //  HANDLE DUPLICATE ENTRY STATE NAME
        State existedState = stateRepository.findByStateName(stateDto.getStateName());
        if(existedState != null)
            throw new DuplicateEntryException(("State with name '"+stateDto.getStateName()+"' already exist!"));

        //  HANDLE IF COUNTRY IS NULL
        int countryId;
        try {
            countryId = stateDto.getCountry().getCountryId();
        }
        catch (Exception ex)
        {
            throw new CustomNullPointerException("Country id should not be empty");
        }

        //  HANDLE IF COUNTRY IS NULL
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id '" + countryId + "' not found"));

        //  IS COUNTRY STATUS ACTIVE ?
        if (!country.isStatus())
            throw new InactiveStatusException("Country with id '" + countryId + "' is not active!");

        stateDto.setStateName(stateDto.getStateName().toUpperCase());
        stateDto.setCountry(appUtils.countryToDto(country));
        return appUtils.stateToDto(stateRepository.save(appUtils.dtoToState(stateDto)));
    }

    @Override
    public StateDto getStateById(int id) {
        return appUtils.stateToDto(stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!")));
    }

    @Override
    public List<StateDto> getAllStates(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<State> pageState = stateRepository.findAll(pageable);

        return appUtils.statesToDtos(pageState.getContent());
    }
    @Override
    public List<StateDto> getAllStatesByDeleted(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<State> pageState = stateRepository.findAllByDeleted(false, pageable);

        return appUtils.statesToDtos(pageState.getContent());
    }

    @Override
    public StateDto updateState(StateDto stateDto, int id) {

        //  HANDLE IF STATE EXIST BY ID
        State state = stateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!"));

        //  HANDLE DUPLICATE ENTRY STATE NAME
        State existedState = stateRepository.findByStateName(stateDto.getStateName());
        if(existedState != null)
            throw new DuplicateEntryException(("State with name '"+stateDto.getStateName()+"' already exist!"));


        //  HANDLE IF COUNTRY IS NULL
        int countryId;
        try {
             countryId = stateDto.getCountry().getCountryId();
        }
        catch (Exception ex){
            throw new CustomNullPointerException("Country id should not be empty!");
        }

        //  HANDLE IF COUNTRY EXIST
        countryRepository.findById(countryId)
                .orElseThrow(()->new ResourceNotFoundException("Country with id '"+countryId+"' not found!"));

        state.setStateName(stateDto.getStateName().toUpperCase());

        return appUtils.stateToDto(stateRepository.save(state));
    }

    @Override
    public String deleteStateById(int id) {

        stateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!"));
        return "State with id " + id + "' deleted!";
    }

    @Override
    public String softDeleteStateById(int id) {
        State state = stateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!"));
        state.setDeleted(true);
        stateRepository.save(state);
        return "State with id " + id + "' Soft deleted!";
    }

    @Override
    public String restoreStateById(int id) {
        State state = stateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!"));
        state.setDeleted(false);
        stateRepository.save(state);
        return "State with id " + id + "' Restored!";
    }

    @Override
    public List<StateDto> getStateByCountryId(int countryId) {

        countryRepository.findById(countryId).orElseThrow(() -> new ResourceNotFoundException("Country with id '" + countryId + "' not found!"));

        return appUtils.statesToDtos(stateRepository.findByCountry_CountryId(countryId));
    }
}
