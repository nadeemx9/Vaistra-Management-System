package com.vaistra.controllers.cscv;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.cscv.SubDistrictDto;
import com.vaistra.services.cscv.SubDistrictService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("subDistrict")
public class SubDistrictController {

    private final SubDistrictService subDistrictService;


    @Autowired
    public SubDistrictController(SubDistrictService subDistrictService) {
        this.subDistrictService = subDistrictService;
    }

    @PostMapping
    public ResponseEntity<SubDistrictDto> addSubDistrict(@Valid @RequestBody SubDistrictDto subDistrictDto)
    {
        return new ResponseEntity<>(subDistrictService.addSubDistrict(subDistrictDto), HttpStatus.CREATED);
    }

    @GetMapping("{subDistrictId}")
    public ResponseEntity<SubDistrictDto> getSubDistrictById(@PathVariable int subDistrictId)
    {
        return new ResponseEntity<>(subDistrictService.getSubDistrictById(subDistrictId), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<HttpResponse> getAllSubDistricts(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                           @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                           @RequestParam(value = "sortBy", defaultValue = "subDistrictId", required = false) String sortBy,
                                                           @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(subDistrictService.getAllSubDistricts(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getAllSubDistrictsByActiveDistricts(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                            @RequestParam(value = "sortBy", defaultValue = "subDistrictId", required = false) String sortBy,
                                                                            @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(subDistrictService.getAllSubDistrictsByActiveDistrict(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @GetMapping("districtId/{districtId}")
    public ResponseEntity<HttpResponse> getAllSubDistrictsByDistrictId(@PathVariable int districtId,
                                                                       @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                       @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue = "subDistrictId", required = false) String sortBy,
                                                                       @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(subDistrictService.getAllSubDistrictsByDistrict(districtId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @GetMapping("stateId/{stateId}")
    public ResponseEntity<HttpResponse> getAlLSubDistrictsByStateId(@PathVariable int stateId,
                                                                       @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                       @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue = "subDistrictId", required = false) String sortBy,
                                                                       @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(subDistrictService.getAlLSubDistrictsByState(stateId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @GetMapping("countryId/{countryId}")
    public ResponseEntity<HttpResponse> getAlLSubDistrictsByCountryId(@PathVariable int countryId,
                                                                    @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                    @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue = "subDistrictId", required = false) String sortBy,
                                                                    @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(subDistrictService.getAllSubDistrictsByCountry(countryId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<HttpResponse> searchSubDistrictByKeyword(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                                                   @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                   @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                   @RequestParam(value = "sortBy", defaultValue = "subDistrictId", required = false) String sortBy,
                                                                   @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(subDistrictService.searchSubDistrictByKeyword(keyword, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @PutMapping("{subDistrictId}")
    public ResponseEntity<SubDistrictDto> updateSubDistrictId(@Valid @RequestBody SubDistrictDto subDistrictDto, @PathVariable int subDistrictId)
    {
        return new ResponseEntity<>(subDistrictService.updateSubDistrict(subDistrictDto, subDistrictId), HttpStatus.OK);
    }

    @DeleteMapping("{subDistrictId}")
    public ResponseEntity<String> deleteSubDistrict(@PathVariable int subDistrictId)
    {
        return new ResponseEntity<>(subDistrictService.deleteSubDistrictById(subDistrictId), HttpStatus.OK);
    }
}
