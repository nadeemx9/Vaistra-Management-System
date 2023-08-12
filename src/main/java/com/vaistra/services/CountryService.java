package com.vaistra.services;

import com.vaistra.payloads.CountryDto;

import java.util.List;

public interface CountryService {
    CountryDto addCountry(CountryDto countryDto);

    CountryDto getCountryById(int id);

    List<CountryDto> getAllCountries(int pageNumber, int pageSize, String sortBy, String sortDirection);

    CountryDto updateCountry(CountryDto country, int id);

    String deleteCountryById(int id);

    String softDeleteCountryById(int id);

    String restoreCountryById(int id);
}
