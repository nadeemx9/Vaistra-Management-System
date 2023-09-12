package com.vaistra.controllers;

import com.vaistra.payloads.DistrictDto;
import com.vaistra.services.DistrictService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("district")
public class DistrictController {

    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService)
    {
        this.districtService = districtService;
    }
    @PostMapping
    public ResponseEntity<DistrictDto> addDistrict(@Valid @RequestBody DistrictDto districtDto)
    {
        return new ResponseEntity<>(districtService.addDistrict(districtDto), HttpStatus.CREATED);
    }

    @GetMapping("{districtId}")
    public ResponseEntity<DistrictDto> getDistrictById(@PathVariable int districtId)
    {
        return new ResponseEntity<>(districtService.getDistrictById(districtId), HttpStatus.FOUND);
    }

    @GetMapping("all")
    public ResponseEntity<List<DistrictDto>> getAllDistricts(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                             @RequestParam(value = "sortBy", defaultValue = "districtId", required = false) String sortBy,
                                                             @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(districtService.getAllDistricts(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.FOUND);
    }
    @GetMapping
    public ResponseEntity<List<DistrictDto>> getAllDistrictsByActive(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                             @RequestParam(value = "sortBy", defaultValue = "districtId", required = false) String sortBy,
                                                             @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(districtService.getAllDistrictsByActiveState(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.FOUND);
    }

    @GetMapping("stateId/{stateId}")
    public ResponseEntity<List<DistrictDto>> getDistrictByStateId(@PathVariable int stateId)
    {
        return new ResponseEntity<>(districtService.getDistrictsByStateId(stateId), HttpStatus.FOUND);
    }
    @GetMapping("countryId/{countryId}")
    public ResponseEntity<List<DistrictDto>> getDistrictByCountryId(@PathVariable int countryId)
    {
        return new ResponseEntity<>(districtService.getDistrictsByCountryId(countryId), HttpStatus.FOUND);
    }
    @PutMapping("{districtId}")
    public ResponseEntity<DistrictDto> updateDistrict(@Valid @RequestBody DistrictDto districtDto, @PathVariable int districtId)
    {
        return new ResponseEntity<>(districtService.updateDistrict(districtDto, districtId), HttpStatus.OK);
    }
//    @PutMapping("softDelete/{districtId}")
//    public ResponseEntity<String> softDeleteDistrictById(@PathVariable int districtId)
//    {
//        return new ResponseEntity<>(districtService.softDeleteDistrictById(districtId), HttpStatus.OK);
//    }

    @DeleteMapping("{districtId}")
    public ResponseEntity<String> hardDeleteDistrictById(@PathVariable int districtId)
    {
        return new ResponseEntity<>(districtService.deleteDistrictById(districtId), HttpStatus.OK);
    }

//    @PutMapping("restore/{districtId}")
//    public ResponseEntity<String> restoreDistrictById(@PathVariable int districtId)
//    {
//        return new ResponseEntity<>(districtService.restoreDistrictById(districtId), HttpStatus.OK);
//    }
}
