package com.vaistra.services.cscv;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.cscv.VillageDto;
import com.vaistra.dto.cscv.VillageUpdateDto;

public interface VillageService {
    VillageDto addVillage(VillageDto villageDto);
    VillageDto getVillageById(int id);
    HttpResponse getAllVillages(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllVillagesByActiveSubDistricts(int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllVillagesBySubDistrict(int subDistrictId, int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAlLVillagesByState(int stateId, int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllVillagesByCountry(int countryId, int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse searchVillagesByKeyword(String keyword, int pageNumber,int pageSize, String sortBy, String sortDirection);
    VillageDto updateVillage(VillageUpdateDto villageDto, int villageId);
    String deleteVillageById(int id);
}
