package com.vaistra.controllers.mastermines;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.VehicleDto;
import com.vaistra.services.mastermines.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<VehicleDto> addVehicle(@Valid @RequestBody VehicleDto vehicleDto)
    {
        return new ResponseEntity<>(vehicleService.addVehicle(vehicleDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getAllVehicles(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "vehicleId", required = false) String sortBy,
                                                       @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(vehicleService.getAllVehicles(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("{vehicleId}")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable int vehicleId)
    {
        return new ResponseEntity<>(vehicleService.getVehicleById(vehicleId), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<HttpResponse> searchVehiclesByKeyword(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                                                @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "vehicleId", required = false) String sortBy,
                                                                @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(vehicleService.searchVehicleByKeyword(keyword, pageNumber, pageSize ,sortBy ,sortDirection), HttpStatus.OK);
    }

    @PutMapping("{vehicleId}")
    public ResponseEntity<VehicleDto> updateVehicle(@Valid @RequestBody VehicleDto vehicleDto, @PathVariable int vehicleId)
    {
        return new ResponseEntity<>(vehicleService.updateVehicle(vehicleDto, vehicleId), HttpStatus.OK);
    }

    @DeleteMapping("{vehicleId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable int vehicleId)
    {
        return new ResponseEntity<>(vehicleService.deleteVehicle(vehicleId), HttpStatus.OK);
    }
}
