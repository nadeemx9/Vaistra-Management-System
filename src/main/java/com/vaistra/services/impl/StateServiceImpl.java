package com.vaistra.services.impl;

import com.vaistra.entities.Country;
import com.vaistra.entities.State;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.InactiveStatusException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.payloads.HttpResponse;
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
        if (!country.isStatus())
            throw new InactiveStatusException("Country with id '" + stateDto.getCountryId() + "' is not active!");

        State state = new State();
        state.setStateName(stateDto.getStateName());
        state.setCountry(country);
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

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
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
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<State> pageState = stateRepository.findByStateNameContainingIgnoreCase(keyword,  pageable);

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

        if(!country.isStatus())
            throw new InactiveStatusException("Country '"+country.getCountryName()+"' is inactive");

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<State> pageState = stateRepository.findByCountry(country, pageable);

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

        stateDto.setStateName(stateDto.getStateName().trim().toUpperCase());

        //  HANDLE IF STATE EXIST BY ID
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!"));

        //  HANDLE DUPLICATE ENTRY STATE NAME
        if(stateRepository.existsByStateName(stateDto.getStateName()))
        {
            throw new DuplicateEntryException("State with name '"+stateDto.getStateName()+"' already exist!");
        }

        state.setStateName(stateDto.getStateName());
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
