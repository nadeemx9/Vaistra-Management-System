package com.vaistra.services.cscv;

import com.vaistra.dto.cscv.CountryDto;
import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.cscv.CountryUpdateDto;
import org.springframework.web.multipart.MultipartFile;

public interface CountryService {
    CountryDto addCountry(CountryDto countryDto);

    CountryDto getCountryById(int id);

    HttpResponse getAllCountries(int pageNumber, int pageSize, String sortBy, String sortDirection);

    HttpResponse getAllCountriesByActive(int pageNumber, int pageSize, String sortBy, String sortDirection);

    CountryDto updateCountry(CountryUpdateDto country, int id);

    String deleteCountryById(int id);

    String softDeleteCountryById(int id);

    String restoreCountryById(int id);

    HttpResponse searchCountry(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);

    String uploadCountryCSV(MultipartFile file);

    String generateCsvData();
}
