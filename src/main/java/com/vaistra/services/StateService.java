package com.vaistra.services;

import com.vaistra.payloads.HttpResponse;
import com.vaistra.payloads.StateDto;

import java.util.List;

public interface StateService {
    StateDto addState(StateDto stateDto);

    StateDto getStateById(int id);

    HttpResponse getAllStates(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllStatesByActiveCountry(int pageNumber, int pageSize, String sortBy, String sortDirection);

    StateDto updateState(StateDto stateDto, int id);

    String deleteStateById(int id);

    String softDeleteStateById(int id);

    String restoreStateById(int id);

    HttpResponse getStatesByCountryId(int countryId, int pageNumber, int pageSize, String sortBy, String sortDirection);

    HttpResponse searchStateByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);
}
