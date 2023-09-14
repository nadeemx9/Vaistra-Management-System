package com.vaistra.services.mastermines;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.EntityDto;

public interface EntityService {
    EntityDto addEntity(EntityDto entityDto);
    EntityDto getEntityById(int entityId);
    HttpResponse getAllEntities(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse searchEntityByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);
    EntityDto updateEntity(EntityDto entityDto, int entityId);
    String deleteEntity(int entityId);
}
