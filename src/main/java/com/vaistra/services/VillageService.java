package com.vaistra.services;

import com.vaistra.entities.SubDistrict;
import com.vaistra.payloads.HttpResponse;
import com.vaistra.payloads.SubDistrictDto;
import com.vaistra.payloads.VillageDto;

public interface VillageService {
    VillageDto addVillage(VillageDto villageDto);
    VillageDto getVillageById(int id);
    HttpResponse getAllVillages(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllVillagesByActiveSubDistricts(int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllVillagesBySubDistrict(int subDistrictId, int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAlLVillagesByState(int stateId, int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllVillagesByCountry(int countryId, int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse searchVillagesByKeyword(String keyword, int pageNumber,int pageSize, String sortBy, String sortDirection);
    VillageDto updateVillage(VillageDto villageDto, int villageId);
    String deleteVillageById(int id);
}
