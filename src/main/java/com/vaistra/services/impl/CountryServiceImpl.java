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

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    //----------------------------------------------------SERVICE METHODS-----------------------------------------------

    @Override
    public CountryDto addCountry(CountryDto c) {

        // HANDLE DUPLICATE NAME ENTRY EXCEPTION
        Country country = countryRepository.findByCountryName(c.getCountryName());
        if(country != null)
            throw new DuplicateEntryException("Country with name '"+c.getCountryName()+"' already exist!");

        c.setCountryName(c.getCountryName().toUpperCase());
        return AppUtils.countryToDto(countryRepository.save(AppUtils.dtoToCountry(c)));
    }

    @Override
    public CountryDto getCountryById(int id) {
        return AppUtils.countryToDto(countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id '" + id + "' Not Found!")));
    }

    @Override
    public List<CountryDto> getAllCountries(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Country> pageCountry = countryRepository.findAll(pageable);
        return AppUtils.countriesToDtos(pageCountry.getContent());
    }

    @Override
    public List<CountryDto> getAllCountriesByDeleted(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Country> pageCountry = countryRepository.findAllByDeleted(false,pageable);

        return AppUtils.countriesToDtos(pageCountry.getContent());
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
        country.setStatus(c.isStatus());
        country.setDeleted(c.isDeleted());
        return AppUtils.countryToDto(countryRepository.save(country));

    }

    @Override
    public String deleteCountryById(int id) {
        countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country with Id '" + id + "' not found!"));

        countryRepository.deleteById(id);
        return "Country with Id '" + id + "' deleted";
    }

    @Override
    public String softDeleteCountryById(int id) {

        Country country = countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country with Id '" + id + "' not found!"));
        country.setDeleted(true);
        countryRepository.save(country);
        return "Country with Id '" + id + "' Soft Deleted";
    }

    @Override
    public String restoreCountryById(int id) {
        Country country = countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country with Id '" + id + "' not found!"));
        country.setDeleted(false);
        countryRepository.save(country);
        return "Country with id '" + id + "' restored!";
    }
}
