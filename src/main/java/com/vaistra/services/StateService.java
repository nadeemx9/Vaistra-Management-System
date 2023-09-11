package com.vaistra.services;

import com.vaistra.payloads.StateDto;

import java.util.List;

public interface StateService {
    StateDto addState(StateDto stateDto);

    StateDto getStateById(int id);

    List<StateDto> getAllStates(int pageNumber, int pageSize, String sortBy, String sortDirection);
    List<StateDto> getAllStatesByActive(int pageNumber, int pageSize, String sortBy, String sortDirection);

    StateDto updateState(StateDto stateDto, int id);

    String deleteStateById(int id);

    String softDeleteStateById(int id);

    String restoreStateById(int id);

    List<StateDto> getStatesByCountryId(int countryId);
}
