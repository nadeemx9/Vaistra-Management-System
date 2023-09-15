package com.vaistra.services.cscv.impl;

import com.vaistra.entities.cscv.Country;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.dto.cscv.CountryDto;
import com.vaistra.dto.cscv.CountryPatchDto;
import com.vaistra.dto.HttpResponse;
import com.vaistra.repositories.cscv.CountryRepository;
import com.vaistra.services.cscv.CountryService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------

    private final CountryRepository countryRepository;
    private final AppUtils appUtils;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, AppUtils appUtils) {
        this.countryRepository = countryRepository;
        this.appUtils = appUtils;
    }


    //----------------------------------------------------SERVICE METHODS-----------------------------------------------

    @Override
    public CountryDto addCountry(CountryDto c) {

        c.setCountryName(c.getCountryName().toUpperCase().trim());

        // HANDLE DUPLICATE NAME ENTRY EXCEPTION
        if(countryRepository.existsByCountryName(c.getCountryName()))
            throw new DuplicateEntryException("Country with name '"+c.getCountryName()+"' already exist!");

        Country country = new Country();
        country.setCountryName(c.getCountryName());
        if(c.getStatus() != null)
            country.setStatus(c.getStatus());
        else
            country.setStatus(true);

        return appUtils.countryToDto(countryRepository.save(country));
    }

    @Override
    public CountryDto getCountryById(int id) {
        return appUtils.countryToDto(countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id '" + id + "' Not Found!")));
    }

    @Override
    public HttpResponse getAllCountries(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Country> pageCountry = countryRepository.findAll(pageable);
        List<CountryDto> countries = appUtils.countriesToDtos(pageCountry.getContent());

       return HttpResponse.builder()
                .pageNumber(pageCountry.getNumber())
                .pageSize(pageCountry.getSize())
                .totalElements(pageCountry.getTotalElements())
                .totalPages(pageCountry.getTotalPages())
                .isLastPage(pageCountry.isLast())
                .data(countries)
                .build();
    }

    @Override
    public HttpResponse getAllCountriesByActive(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Country> pageCountry = countryRepository.findAllByStatus(true,pageable);
        List<CountryDto> countries = appUtils.countriesToDtos(pageCountry.getContent());

        return HttpResponse.builder()
                .pageNumber(pageCountry.getNumber())
                .pageSize(pageCountry.getSize())
                .totalElements(pageCountry.getTotalElements())
                .totalPages(pageCountry.getTotalPages())
                .isLastPage(pageCountry.isLast())
                .data(countries)
                .build();
    }

    @Override
    public HttpResponse searchCountry(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {

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

        Page<Country> pageCountry = countryRepository.findByCountryIdOrStatusOrCountryNameContainingIgnoreCase(intKeyword, booleanKeyword, keyword, pageable);
        List<CountryDto> countries = appUtils.countriesToDtos(pageCountry.getContent());
        return HttpResponse.builder()
                .pageNumber(pageCountry.getNumber())
                .pageSize(pageCountry.getSize())
                .totalElements(pageCountry.getTotalElements())
                .totalPages(pageCountry.getTotalPages())
                .isLastPage(pageCountry.isLast())
                .data(countries)
                .build();
    }

    @Override
    public CountryDto updateCountry(CountryDto c, int id) {

        // HANDLE IF COUNTRY EXIST BY ID
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country with Id '" + id + "' not found!"));

        // HANDLE DUPLICATE ENTRY EXCEPTION
        if(c.getCountryName() != null)
        {
            Country countryWithSameName = countryRepository.findByCountryNameIgnoreCase(c.getCountryName().trim());

            if(countryWithSameName != null && !countryWithSameName.getCountryId().equals(country.getCountryId()))
                throw new DuplicateEntryException("Country '"+c.getCountryName()+"' already exist!");
            country.setCountryName(c.getCountryName().trim().toUpperCase());
        }

        if(c.getStatus() != null)
            country.setStatus(c.getStatus());

        return appUtils.countryToDto(countryRepository.save(country));
    }

    @Override
    public String deleteCountryById(int id) {
        countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country with Id '" + id + "' not found!"));

        countryRepository.deleteById(id);
        return "Country with Id '" + id + "' deleted";
    }

    @Override
    public String softDeleteCountryById(int id) {

//        Country country = countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country with Id '" + id + "' not found!"));
//        country.setDeleted(true);
//        countryRepository.save(country);
//        return "Country with Id '" + id + "' Soft Deleted";

        return null;
    }

    @Override
    public String restoreCountryById(int id) {
//        Country country = countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country with Id '" + id + "' not found!"));
//        country.setDeleted(false);
//        countryRepository.save(country);
//        return "Country with id '" + id + "' restored!";

        return null;

    }


}
