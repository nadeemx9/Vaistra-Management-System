package com.vaistra.services.mastermines;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.VehicleDto;
import com.vaistra.dto.mastermines.VehicleUpdateDto;

public interface VehicleService {

    VehicleDto addVehicle(VehicleDto vehicleDto);
    VehicleDto getVehicleById(int vehicleId);
    HttpResponse getAllVehicles(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse searchVehicleByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);
    VehicleDto updateVehicle(VehicleUpdateDto vehicleDto, int vehicleId);
    String deleteVehicle(int vehicleId);
}
