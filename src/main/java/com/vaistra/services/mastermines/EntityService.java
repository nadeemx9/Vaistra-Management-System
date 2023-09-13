package com.vaistra.services.mastermines;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.EntityDto;
import com.vaistra.dto.mastermines.MineralDto;

public interface EntityService {
    EntityDto addEntity(EntityDto entityDto);
    MineralDto getEntityById(int entityId);
    HttpResponse getAllEntities(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse searchEntityByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);
    EntityDto updateEntity(EntityDto entityDto, int entityId);
    String deleteEntity(int entityId);
}
