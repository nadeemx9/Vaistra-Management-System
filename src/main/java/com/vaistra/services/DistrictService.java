package com.vaistra.services;

import com.vaistra.payloads.DistrictDto;
import com.vaistra.payloads.HttpResponse;

import java.util.List;

public interface DistrictService {
    DistrictDto addDistrict(DistrictDto districtDto);

    DistrictDto getDistrictById(int id);

    HttpResponse getAllDistricts(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllDistrictsByActiveState(int pageNumber, int pageSize, String sortBy, String sortDirection);

    DistrictDto updateDistrict(DistrictDto districtDto, int id);

    String deleteDistrictById(int id);

//    String softDeleteDistrictById(int id);
//
//    String restoreDistrictById(int id);

    HttpResponse getDistrictsByStateId(int stateId, int pageNumber, int pageSize, String sortBy, String sortDirection);

    HttpResponse getDistrictsByCountryId(int countryId, int pageNumber, int pageSize, String sortBy, String sortDirection);

    HttpResponse searchDistrictByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);
}
