package com.vaistra.services.cscv;

import com.vaistra.dto.cscv.DistrictDto;
import com.vaistra.dto.HttpResponse;

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
