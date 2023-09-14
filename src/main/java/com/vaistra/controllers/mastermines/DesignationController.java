package com.vaistra.controllers.mastermines;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.DesignationDto;
import com.vaistra.services.mastermines.DesignationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("designation")
public class DesignationController {

    private final DesignationService designationService;

    @Autowired
    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @PostMapping
    public ResponseEntity<DesignationDto> addDesignation(@Valid @RequestBody DesignationDto designationDto)
    {
        return new ResponseEntity<>(designationService.addDesignation(designationDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getAllDesignations(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                           @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                           @RequestParam(value = "sortBy", defaultValue = "designationId", required = false) String sortBy,
                                                           @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(designationService.getAllDesignations(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("{designationId}")
    public ResponseEntity<DesignationDto> getDesignationById(@PathVariable int designationId)
    {
        return new ResponseEntity<>(designationService.getDesignationById(designationId), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<HttpResponse> searchDesignationsByKeyword(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                                                    @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue = "designationId", required = false) String sortBy,
                                                                    @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(designationService.searchDesignationsByKeyword(keyword, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @PutMapping("{designationId}")
    public ResponseEntity<DesignationDto> updateDesignation(@Valid @RequestBody DesignationDto designationDto, @PathVariable int designationId)
    {
        return new ResponseEntity<>(designationService.updateDesignation(designationDto, designationId), HttpStatus.OK);
    }

    @DeleteMapping("{designationId}")
    public ResponseEntity<String> deleteDesignation(@PathVariable int designationId)
    {
        return new ResponseEntity<>(designationService.deleteDesignation(designationId), HttpStatus.OK);
    }
}
