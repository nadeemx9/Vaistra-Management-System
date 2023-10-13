package com.vaistra.services.cscv.impl;

import com.vaistra.dto.cscv.StateUpdateDto;
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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

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

        Pageable pageable = PageRequest.of(pageNumber, Integer.MAX_VALUE, sort);
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
    public StateDto updateState(StateUpdateDto stateDto, int id) {

        //  HANDLE IF STATE EXIST BY ID
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State with id '" + id + "' not found!"));

        if(stateDto.getStateName() != null)
        {
            State stateWithSameName = stateRepository.findByStateNameIgnoreCase(stateDto.getStateName().trim());

            if(stateWithSameName != null && !stateWithSameName.getStateId().equals(state.getStateId()))
                throw new DuplicateEntryException("State '"+stateDto.getStateName()+"' already exist!");

            state.setStateName(stateDto.getStateName().trim().toUpperCase());
        }

        if(stateDto.getCountryId() != null)
        {
            Country country = countryRepository.findById(stateDto.getCountryId())
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


    public String uploadStateCSV(MultipartFile file) {
        if(file.isEmpty()){
            throw new ResourceNotFoundException("State CSV File not found...!");
        }
        if(!Objects.equals(file.getContentType(), "text/csv")){
            throw new IllegalArgumentException("Invalid file type. Please upload a CSV file.");
        }

        try {
            List<State> states = CSVParser.parse(file.getInputStream(), Charset.defaultCharset(), CSVFormat.DEFAULT)
                    .stream().skip(1) // Skip the first row
                    .map(record -> {
                        String stateName = record.get(1).trim();
                        Boolean isActive = Boolean.parseBoolean(record.get(2));

                        Optional<State> existState = Optional.ofNullable(stateRepository.findByStateNameIgnoreCase(stateName));

                        if(existState.isPresent()){
                            return null;
                        }

                        else {
                            State state = new State();
                            state.setStateName(stateName); // Assuming the first column is "stateName"
                            state.setStatus(isActive); // Assuming the second column is "isActive"
                            // Get the country name from the CSV file
                            String countryName = record.get(0).trim(); // Assuming the third column is "countryName"

                            // Try to find the country by name
                            Country country = countryRepository.findByCountryNameIgnoreCase(countryName);

                            // If the country does not exist, create a new one
                            if (country == null) {
                                country = new Country();
                                country.setCountryName(countryName.trim());
                                country.setStatus(true); // You can set the "isActive" flag as needed
                                countryRepository.save(country);
                            }

                            state.setCountry(country);
                            return state;
                        }

                    })
                    .toList();

            // Filter out duplicates by country name
            List<State> nonDuplicateState = states.stream()
                    .filter(distinctByKey(State::getStateName))
                    .toList();

            // Save the non-duplicate entities to the database
            long uploadedRecordCount = nonDuplicateState.size();
            stateRepository.saveAll(nonDuplicateState);

            return "CSV file uploaded successfully. " + uploadedRecordCount + " records uploaded.";


        }catch (Exception e){
            return e.getMessage();
        }

    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
