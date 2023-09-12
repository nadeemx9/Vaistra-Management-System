package com.vaistra.services;

import com.vaistra.payloads.CountryDto;
import com.vaistra.payloads.HttpResponse;

import java.util.List;

public interface CountryService {
    CountryDto addCountry(CountryDto countryDto);

    CountryDto getCountryById(int id);

    HttpResponse getAllCountries(int pageNumber, int pageSize, String sortBy, String sortDirection);

    HttpResponse getAllCountriesByActive(int pageNumber, int pageSize, String sortBy, String sortDirection);

    CountryDto updateCountry(CountryDto country, int id);

    String deleteCountryById(int id);

    String softDeleteCountryById(int id);

    String restoreCountryById(int id);

    HttpResponse searchCountry(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);


}
