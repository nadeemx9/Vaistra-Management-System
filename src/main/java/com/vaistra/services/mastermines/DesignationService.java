package com.vaistra.services.mastermines;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.DesignationDto;
import com.vaistra.dto.mastermines.DesignationUpdateDto;


public interface DesignationService {


    DesignationDto addDesignation(DesignationDto designationDto);
    DesignationDto getDesignationById(int designationId);
    HttpResponse getAllDesignations(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse searchDesignationsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);
    DesignationDto updateDesignation(DesignationUpdateDto designationDto, int designationId);
    String deleteDesignation(int designationId);
}
