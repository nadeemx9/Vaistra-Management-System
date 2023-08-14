package com.vaistra.services.impl;

import com.vaistra.entities.Country;
import com.vaistra.entities.State;
import com.vaistra.exception.InactiveStatusException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.payloads.StateDto;
import com.vaistra.repositories.CountryRepository;
import com.vaistra.repositories.StateRepository;
import com.vaistra.services.StateService;
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

    @Autowired
    public StateServiceImpl(StateRepository stateRepository, CountryRepository countryRepository) {
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }


    //------------------------------------------------------UTILITY METHODS---------------------------------------------
    public static StateDto stateToDto(State state) {
        return new StateDto(state.getStateId(), state.getStateName(), state.isStatus(), state.isDeleted(), CountryServiceImpl.countryToDto(state.getCountry()));
    }

    public static State dtoToState(StateDto dto) {
        return new State(dto.getStateId(), dto.getStateName(), dto.isStatus(), dto.isDeleted(), CountryServiceImpl.dtoToCountry(dto.getCountry()));
    }

    public static List<StateDto> statesToDtos(List<State> states) {
        List<StateDto> dtos = new ArrayList<>();

        for (State s : states)
            dtos.add(new StateDto(s.getStateId(), s.getStateName(), s.isStatus(), s.isDeleted(), CountryServiceImpl.countryToDto(s.getCountry())));

        return dtos;
    }

    public static List<State> dtosToStates(List<StateDto> dtos) {
        List<State> states = new ArrayList<>();
        for (StateDto d : dtos)
            states.add(new State(d.getStateId(), d.getStateName(), d.isStatus(), d.isDeleted(), CountryServiceImpl.dtoToCountry(d.getCountry())));

        return states;
    }


    //----------------------------------------------------SERVICE METHODS-----------------------------------------------
    @Override
    public StateDto addState(StateDto stateDto) {
        int countryId = stateDto.getCountry().getCountryId();

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id '" + countryId + "' not found"));

        //  IS COUNTRY STATUS ACTIVE ?
        if (!country.isStatus())
            throw new InactiveStatusException("Country with id '" + countryId + "' is not active!");

        stateDto.setStateName(stateDto.getStateName().toUpperCase());
        stateDto.setCountry(CountryServiceImpl.countryToDto(country));
        return stateToDto(stateRepository.save(dtoToState(stateDto)));
    }

    @Override
    public StateDto getStateById(int id) {
        return stateToDto(stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!")));
    }

    @Override
    public List<StateDto> getAllStates(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<State> pageState = stateRepository.findAll(pageable);

        return statesToDtos(pageState.getContent());
    }

    @Override
    public StateDto updateState(StateDto stateDto, int id) {

        State state = stateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!"));
        state.setStateName(stateDto.getStateName());
        state.setStatus(stateDto.isStatus());
        state.setDeleted(stateDto.isDeleted());
        state.setCountry(CountryServiceImpl.dtoToCountry(stateDto.getCountry()));

        return stateToDto(stateRepository.save(state));
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
}