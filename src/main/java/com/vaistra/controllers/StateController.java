package com.vaistra.controllers;

import com.vaistra.payloads.StateDto;
import com.vaistra.services.StateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("state")
public class StateController {


    //---------------------------------------------------CONSTRUCTOR INJECTION------------------------------------------
    private final StateService stateService;

    @Autowired
    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    //---------------------------------------------------URL ENDPOINTS--------------------------------------------------

    @PostMapping
    public ResponseEntity<StateDto> addState(@Valid @RequestBody StateDto dto) {
        return new ResponseEntity<>(stateService.addState(dto), HttpStatus.CREATED);
    }

    @GetMapping("{stateId}")
    public ResponseEntity<StateDto> getStateById(@PathVariable int stateId) {
        return new ResponseEntity<>(stateService.getStateById(stateId), HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<List<StateDto>> getAllStates(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                       @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "stateId", required = false) String sortBy,
                                                       @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection) {
        return new ResponseEntity<>(stateService.getAllStates(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.FOUND);
    }

    @PutMapping("{stateId}")
    public ResponseEntity<StateDto> updateState(@Valid @RequestBody StateDto stateDto, @PathVariable int stateId) {
        return new ResponseEntity<>(stateService.updateState(stateDto, stateId), HttpStatus.OK);
    }

    @DeleteMapping("hardDelete/{stateId}")
    public ResponseEntity<String> deleteStateById(@PathVariable int stateId) {
        return new ResponseEntity<>(stateService.deleteStateById(stateId), HttpStatus.OK);
    }

    @DeleteMapping("{stateId}")
    public ResponseEntity<String> softDeleteStateById(@PathVariable int stateId) {
        return new ResponseEntity<>(stateService.softDeleteStateById(stateId), HttpStatus.OK);
    }

    @PutMapping("restore/{stateId}")
    public ResponseEntity<String> restoreStateById(@PathVariable int stateId) {
        return new ResponseEntity<>(stateService.restoreStateById(stateId), HttpStatus.OK);
    }

    @GetMapping("countryId/{countryId}")
    public ResponseEntity<List<StateDto>> getStateByCountryId(@PathVariable int countryId)
    {
        return new ResponseEntity<>(stateService.getStateByCountryId(countryId), HttpStatus.OK);
    }
}
