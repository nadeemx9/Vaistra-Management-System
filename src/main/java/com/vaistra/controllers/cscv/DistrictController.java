package com.vaistra.controllers.cscv;

import com.vaistra.dto.cscv.DistrictDto;
import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.cscv.DistrictUpdateDto;
import com.vaistra.services.cscv.DistrictService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(districtService.getDistrictById(districtId), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<HttpResponse> getAllDistricts(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "2147483647", required = false) Integer pageSize,
                                                             @RequestParam(value = "sortBy", defaultValue = "districtId", required = false) String sortBy,
                                                             @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(districtService.getAllDistricts(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<HttpResponse> getAllDistrictsByActiveState(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                             @RequestParam(value = "sortBy", defaultValue = "districtId", required = false) String sortBy,
                                                             @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(districtService.getAllDistrictsByActiveState(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("stateId/{stateId}")
    public ResponseEntity<HttpResponse> getDistrictsByStateId(@PathVariable int stateId,
                                                             @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                             @RequestParam(value = "sortBy", defaultValue = "districtId", required = false) String sortBy,
                                                             @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(districtService.getDistrictsByStateId(stateId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @GetMapping("countryId/{countryId}")
    public ResponseEntity<HttpResponse> getDistrictsByCountryId(@PathVariable int countryId,
                                                                @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "districtId", required = false) String sortBy,
                                                                @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(districtService.getDistrictsByCountryId(countryId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<HttpResponse> searchDistrictsByKeyword(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                                                @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "districtId", required = false) String sortBy,
                                                                @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(districtService.searchDistrictByKeyword(keyword, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @PutMapping("{districtId}")
    public ResponseEntity<DistrictDto> updateDistrict(@Valid @RequestBody DistrictUpdateDto districtDto, @PathVariable int districtId)
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
