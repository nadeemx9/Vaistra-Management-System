package com.vaistra.controllers.mastermines;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.EquipmentDto;
import com.vaistra.services.mastermines.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    public ResponseEntity<EquipmentDto> addEquipment(@Valid @RequestBody EquipmentDto equipmentDto)
    {
        return new ResponseEntity<>(equipmentService.addEquipment(equipmentDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getAllEquipments(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                         @RequestParam(value = "sortBy", defaultValue = "equipmentId", required = false) String sortBy,
                                                         @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(equipmentService.getAllEquipments(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("{equipmentId}")
    public ResponseEntity<EquipmentDto> getEquipmentById(@PathVariable int equipmentId)
    {
        return new ResponseEntity<>(equipmentService.getEquipmentById(equipmentId), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<HttpResponse> searchEquipmentsByKeyword(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                                                  @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                  @RequestParam(value = "sortBy", defaultValue = "equipmentId", required = false) String sortBy,
                                                                  @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(equipmentService.searchEquipmentsByKeyword(keyword, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @PutMapping("{equipmentId}")
    public ResponseEntity<EquipmentDto> updateEquipment(@Valid @RequestBody EquipmentDto equipmentDto, @PathVariable int equipmentId)
    {
        return new ResponseEntity<>(equipmentService.updateEquipment(equipmentDto, equipmentId), HttpStatus.OK);
    }

    @DeleteMapping("{equipmentId}")
    public ResponseEntity<String> deleteEquipment(@PathVariable int equipmentId)
    {
        return new ResponseEntity<>(equipmentService.deleteEquipment(equipmentId), HttpStatus.OK);
    }
}
