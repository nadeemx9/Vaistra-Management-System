package com.vaistra.services;

import com.vaistra.entities.SubDistrict;
import com.vaistra.payloads.HttpResponse;
import com.vaistra.payloads.SubDistrictDto;

public interface SubDistrictService {

    SubDistrictDto addSubDistrict(SubDistrictDto subDistrictDto);
    SubDistrictDto getSubDistrictById(int id);
    HttpResponse getAllSubDistricts(int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllSubDistrictsByActiveDistrict(int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllSubDistrictsByDistrict(int districtId, int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAlLSubDistrictsByState(int stateId, int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllSubDistrictsByCountry(int countryId, int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse searchSubDistrictByKeyword(String keyword,int pageNumber,int pageSize, String sortBy, String sortDirection);
    SubDistrictDto updateSubDistrict(SubDistrictDto subDistrictDto, int id);
    String deleteSubDistrictById(int id);

}
