package com.vaistra.services.impl;

import com.vaistra.entities.Country;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.payloads.CountryDto;
import com.vaistra.repositories.CountryRepository;
import com.vaistra.services.CountryService;
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
        country.setStatus(true);

        return appUtils.countryToDto(countryRepository.save(country));
    }

    @Override
    public CountryDto getCountryById(int id) {
        return appUtils.countryToDto(countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id '" + id + "' Not Found!")));
    }

    @Override
    public List<CountryDto> getAllCountries(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Country> pageCountry = countryRepository.findAll(pageable);
        return appUtils.countriesToDtos(pageCountry.getContent());
    }

    @Override
    public List<CountryDto> getAllCountriesByActive(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Country> pageCountry = countryRepository.findAllByStatus(true,pageable);

        return appUtils.countriesToDtos(pageCountry.getContent());
    }

    @Override
    public CountryDto updateCountry(CountryDto c, int id) {
        // HANDLE IF COUNTRY EXIST BY ID
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country with Id '" + id + "' not found!"));


        // HANDLE DUPLICATE ENTRY EXCEPTION
        Country existedCountry = countryRepository.findByCountryName(c.getCountryName());
        if(existedCountry != null)
            throw new DuplicateEntryException("Country with name '"+c.getCountryName()+"' already exist!");

        country.setCountryName(c.getCountryName().toUpperCase());

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
