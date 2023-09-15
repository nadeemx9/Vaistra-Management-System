package com.vaistra.services.cscv;

import com.vaistra.dto.cscv.CountryDto;
import com.vaistra.dto.cscv.CountryPatchDto;
import com.vaistra.dto.HttpResponse;

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
