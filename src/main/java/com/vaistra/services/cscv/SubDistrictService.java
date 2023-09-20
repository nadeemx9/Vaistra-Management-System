package com.vaistra.services.cscv;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.cscv.SubDistrictDto;
import com.vaistra.dto.cscv.SubDistrictUpdateDto;

public interface SubDistrictService {

    SubDistrictDto addSubDistrict(SubDistrictDto subDistrictDto);
    SubDistrictDto getSubDistrictById(int id);
    HttpResponse getAllSubDistricts(int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllSubDistrictsByActiveDistrict(int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllSubDistrictsByDistrict(int districtId, int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllSubDistrictsByState(int stateId, int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllSubDistrictsByCountry(int countryId, int pageNumber,int pageSize, String sortBy, String sortDirection);
    HttpResponse searchSubDistrictByKeyword(String keyword,int pageNumber,int pageSize, String sortBy, String sortDirection);
    SubDistrictDto updateSubDistrict(SubDistrictUpdateDto subDistrictDto, int id);
    String deleteSubDistrictById(int id);

}
