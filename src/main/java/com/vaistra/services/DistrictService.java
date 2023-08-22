package com.vaistra.services;

import com.vaistra.payloads.DistrictDto;
import com.vaistra.payloads.StateDto;

import java.util.List;

public interface DistrictService {
    DistrictDto addDistrict(DistrictDto districtDto);

    DistrictDto getDistrictById(int id);

    List<DistrictDto> getAllDistricts(int pageNumber, int pageSize, String sortBy, String sortDirection);
    List<DistrictDto> getAllDistrictsByDeleted(int pageNumber, int pageSize, String sortBy, String sortDirection);


    DistrictDto updateDistrict(DistrictDto districtDto, int id);

    String deleteDistrictById(int id);

    String softDeleteDistrictById(int id);

    String restoreDistrictById(int id);

    List<DistrictDto> getDistrictByStateId(int stateId);

    List<DistrictDto> getDistrictByCountryId(int countryId);
}
