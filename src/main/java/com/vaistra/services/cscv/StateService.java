package com.vaistra.services.cscv;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.cscv.StateDto;
import com.vaistra.dto.cscv.StateUpdateDto;

public interface StateService {
    StateDto addState(StateDto stateDto);

    StateDto getStateById(int id);

    HttpResponse getAllStates(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllStatesByActiveCountry(int pageNumber, int pageSize, String sortBy, String sortDirection);

    StateDto updateState(StateUpdateDto stateDto, int id);

    String deleteStateById(int id);

    String softDeleteStateById(int id);

    String restoreStateById(int id);

    HttpResponse getStatesByCountryId(int countryId, int pageNumber, int pageSize, String sortBy, String sortDirection);

    HttpResponse searchStateByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);
}
