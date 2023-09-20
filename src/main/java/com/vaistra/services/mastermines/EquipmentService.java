package com.vaistra.services.mastermines;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.EquipmentDto;
import com.vaistra.dto.mastermines.EquipmentUpdateDto;

public interface EquipmentService {

    EquipmentDto addEquipment(EquipmentDto equipmentDto);
    EquipmentDto getEquipmentById(int equipmentId);
    HttpResponse getAllEquipments(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse searchEquipmentsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);
    EquipmentDto updateEquipment(EquipmentUpdateDto equipmentDto, int equipmentId);
    String deleteEquipment(int equipmentId);
}
