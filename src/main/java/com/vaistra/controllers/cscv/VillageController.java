package com.vaistra.controllers.cscv;


import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.cscv.VillageDto;
import com.vaistra.dto.cscv.VillageUpdateDto;
import com.vaistra.services.cscv.VillageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("village")
public class VillageController {

    private final VillageService villageService;

    @Autowired
    public VillageController(VillageService villageService) {
        this.villageService = villageService;
    }

    @PostMapping
    public ResponseEntity<VillageDto> addVillage(@Valid @RequestBody VillageDto villageDto)
    {
        return new ResponseEntity<>(villageService.addVillage(villageDto), HttpStatus.CREATED);
    }

    @GetMapping("{villageId}")
    public ResponseEntity<VillageDto> getVillageById(@PathVariable int villageId)
    {
        return new ResponseEntity<>(villageService.getVillageById(villageId), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<HttpResponse> getAlLVillages(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                       @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "villageId", required = false) String sortBy,
                                                       @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(villageService.getAllVillages(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getAllVillagesByActiveSubDistricts(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                           @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                           @RequestParam(value = "sortBy", defaultValue = "villageId", required = false) String sortBy,
                                                                           @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(villageService.getAllVillagesByActiveSubDistricts(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("subDistrictId/{subDistrictId}")
    public ResponseEntity<HttpResponse> getAlLVillagesBySubDistrictId(@PathVariable int subDistrictId,
                                                                      @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                      @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                      @RequestParam(value = "sortBy", defaultValue = "villageId", required = false) String sortBy,
                                                                      @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(villageService.getAllVillagesBySubDistrict(subDistrictId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("stateId/{stateId}")
    public ResponseEntity<HttpResponse> getAlLVillagesByStateId(@PathVariable int stateId,
                                                                      @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                      @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                      @RequestParam(value = "sortBy", defaultValue = "villageId", required = false) String sortBy,
                                                                      @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(villageService.getAlLVillagesByState(stateId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @GetMapping("countryId/{countryId}")
    public ResponseEntity<HttpResponse> getAlLVillagesByCountryId(@PathVariable int countryId,
                                                                @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "villageId", required = false) String sortBy,
                                                                @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(villageService.getAllVillagesByCountry(countryId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<HttpResponse> searchVillagesByKeyword(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                                                @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "villageId", required = false) String sortBy,
                                                                @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(villageService.searchVillagesByKeyword(keyword, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @PutMapping("{villageId}")
    public ResponseEntity<VillageDto> updateVillage(@Valid @RequestBody VillageUpdateDto villageDto, @PathVariable int villageId)
    {
        return new ResponseEntity<>(villageService.updateVillage(villageDto, villageId), HttpStatus.OK);
    }

    @DeleteMapping("{villageId}")
    public ResponseEntity<String> deleteVillageById(@PathVariable int villageId)
    {
        return new ResponseEntity<>(villageService.deleteVillageById(villageId), HttpStatus.OK);
    }


}
