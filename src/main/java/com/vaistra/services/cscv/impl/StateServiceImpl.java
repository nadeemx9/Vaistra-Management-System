package com.vaistra.services.cscv.impl;

import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.State;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.InactiveStatusException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.cscv.StateDto;
import com.vaistra.repositories.cscv.CountryRepository;
import com.vaistra.repositories.cscv.StateRepository;
import com.vaistra.services.cscv.StateService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

        stateDto.setStateName(stateDto.getStateName().trim().toUpperCase());

        //  HANDLE DUPLICATE ENTRY STATE NAME
        if(stateRepository.existsByStateName(stateDto.getStateName()))
            throw new DuplicateEntryException(("State with name '"+stateDto.getStateName()+"' already exist!"));

        //  HANDLE IF COUNTRY IS NULL
        Country country = countryRepository.findById(stateDto.getCountryId())
                .orElseThrow(()->new ResourceNotFoundException("Country with ID '"+stateDto.getCountryId()+"' not found!"));

        //  IS COUNTRY STATUS ACTIVE ?
        if (!country.getStatus())
            throw new InactiveStatusException("Country with id '" + stateDto.getCountryId() + "' is not active!");

        State state = new State();
        state.setStateName(stateDto.getStateName());
        state.setCountry(country);
        if(stateDto.getStatus() != null)
            state.setStatus(stateDto.getStatus());
        else
            state.setStatus(true);

        return appUtils.stateToDto(stateRepository.save(state));
    }

    @Override
    public StateDto getStateById(int id) {
        return appUtils.stateToDto(stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!")));
    }

    @Override
    public HttpResponse getAllStates(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<State> pageState = stateRepository.findAll(pageable);
        List<StateDto> states = appUtils.statesToDtos(pageState.getContent());

        return HttpResponse.builder()
                .pageNumber(pageState.getNumber())
                .pageSize(pageState.getSize())
                .totalElements(pageState.getTotalElements())
                .totalPages(pageState.getTotalPages())
                .isLastPage(pageState.isLast())
                .data(states)
                .build();
    }
    @Override
    public HttpResponse getAllStatesByActiveCountry(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, Integer.MAX_VALUE, sort);

        Page<State> pageState = stateRepository.findAllByCountry_Status(true,  pageable);

        List<StateDto> states = appUtils.statesToDtos(pageState.getContent());

        return HttpResponse.builder()
                .pageNumber(pageState.getNumber())
                .pageSize(pageState.getSize())
                .totalElements(pageState.getTotalElements())
                .totalPages(pageState.getTotalPages())
                .isLastPage(pageState.isLast())
                .data(states)
                .build();
    }

    @Override
    public HttpResponse searchStateByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Integer intKeyword = null;
        Boolean booleanKeyword = null;

        if(keyword.equalsIgnoreCase("true"))
            booleanKeyword = Boolean.TRUE;
        else if (keyword.equalsIgnoreCase("false"))
            booleanKeyword = Boolean.FALSE;

        try {
            intKeyword = Integer.parseInt(keyword);
        }catch (Exception e){

        }

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<State> pageState = stateRepository.findAllByStateIdOrStatusOrStateNameContainingIgnoreCaseOrCountry_CountryNameContainingIgnoreCase(intKeyword, booleanKeyword, keyword, keyword,  pageable);

        List<StateDto> states = appUtils.statesToDtos(pageState.getContent());

        return HttpResponse.builder()
                .pageNumber(pageState.getNumber())
                .pageSize(pageState.getSize())
                .totalElements(pageState.getTotalElements())
                .totalPages(pageState.getTotalPages())
                .isLastPage(pageState.isLast())
                .data(states)
                .build();
    }

    @Override
    public HttpResponse getStatesByCountryId(int countryId, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id '" + countryId + "' not found!"));

        if(!country.getStatus())
            throw new InactiveStatusException("Country '"+country.getCountryName()+"' is inactive");

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<State> pageState = stateRepository.findAllByCountry(country, pageable);

        List<StateDto> states = appUtils.statesToDtos(pageState.getContent());

        return HttpResponse.builder()
                .pageNumber(pageState.getNumber())
                .pageSize(pageState.getSize())
                .totalElements(pageState.getTotalElements())
                .totalPages(pageState.getTotalPages())
                .isLastPage(pageState.isLast())
                .data(states)
                .build();
    }

    @Override
    public StateDto updateState(StateDto stateDto, int id) {

        //  HANDLE IF STATE EXIST BY ID
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!"));

        if(stateDto.getStateName() != null)
        {
            State stateWithSameName = stateRepository.findByStateNameIgnoreCase(stateDto.getStateName().trim());

            if(stateWithSameName != null && !stateWithSameName.getStateId().equals(state.getStateId()))
                throw new DuplicateEntryException("Country '"+stateDto.getStateName()+"' already exist!");

            state.setStateName(stateDto.getStateName().trim().toUpperCase());
        }

        if(stateDto.getCountryId() != null)
        {
            Country country = countryRepository.findById(stateDto.getStateId())
                    .orElseThrow(()->new ResourceNotFoundException("Country with ID '"+stateDto.getCountryId()+"' not found!"));

            state.setCountry(country);
        }

        return appUtils.stateToDto(stateRepository.save(state));
    }

    @Override
    public String deleteStateById(int id) {

        State state = stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!"));

        stateRepository.delete(state);
        return "State with id " + id + "' deleted!";
    }

    @Override
    public String softDeleteStateById(int id) {
//        State state = stateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!"));
//        state.setDeleted(true);
//        stateRepository.save(state);
//        return "State with id " + id + "' Soft deleted!";

        return null;
    }

    @Override
    public String restoreStateById(int id) {
//        State state = stateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!"));
//        state.setDeleted(false);
//        stateRepository.save(state);
//        return "State with id " + id + "' Restored!";

        return null;
    }


}
