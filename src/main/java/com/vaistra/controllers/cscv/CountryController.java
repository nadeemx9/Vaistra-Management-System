package com.vaistra.controllers.cscv;

import com.vaistra.dto.cscv.CountryDto;
import com.vaistra.dto.cscv.CountryPatchDto;
import com.vaistra.dto.HttpResponse;
import com.vaistra.services.cscv.CountryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("country")
public class CountryController {

    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }


    @PostMapping
    public ResponseEntity<CountryDto> addCountry(@Valid @RequestBody CountryDto country) {
        return new ResponseEntity<>(countryService.addCountry(country), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getAllCountriesByActive(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "countryId", required = false) String sortBy,
                                                                @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection) {

        return new ResponseEntity<>(countryService.getAllCountriesByActive(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @GetMapping("all")
    public ResponseEntity<HttpResponse> getAllCountries(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                            @RequestParam(value = "sortBy", defaultValue = "countryId", required = false) String sortBy,
                                                            @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection) {

        return new ResponseEntity<>(countryService.getAllCountries(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("{countryId}")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable int countryId) {
        return new ResponseEntity<>(countryService.getCountryById(countryId), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<HttpResponse> searchByKeyword(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                                        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                        @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                        @RequestParam(value = "sortBy", defaultValue = "countryId", required = false) String sortBy,
                                                        @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(countryService.searchCountry(keyword, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @PutMapping("{countryId}")
    public ResponseEntity<CountryDto> updateCountry(@RequestBody CountryDto country, @PathVariable int countryId) {
        return new ResponseEntity<>(countryService.updateCountry(country, countryId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{countryId}")
    public ResponseEntity<String> deleteCountryById(@PathVariable int countryId) {
        return new ResponseEntity<>(countryService.deleteCountryById(countryId), HttpStatus.OK);
    }

    @PutMapping("softDelete/{countryId}")
    public ResponseEntity<String> softDeleteById(@PathVariable int countryId) {
        return new ResponseEntity<>(countryService.softDeleteCountryById(countryId), HttpStatus.OK);
    }


    @PutMapping("restore/{countryId}")
    public ResponseEntity<String> restoreCountryById(@PathVariable int countryId) {
        return new ResponseEntity<>(countryService.restoreCountryById(countryId), HttpStatus.OK);
    }

}
