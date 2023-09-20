package com.vaistra.services.mastermines.impl;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.EntityDto;
import com.vaistra.dto.mastermines.EntityUpdateDto;
import com.vaistra.dto.mastermines.MineralDto;
import com.vaistra.entities.mastermines.EntityTbl;
import com.vaistra.entities.mastermines.Mineral;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.mastermines.EntityRepository;
import com.vaistra.services.mastermines.EntityService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityServiceImpl implements EntityService {

    private final EntityRepository entityRepository;
    private final AppUtils appUtils;

    @Autowired
    public EntityServiceImpl(EntityRepository entityRepository, AppUtils appUtils) {
        this.entityRepository = entityRepository;
        this.appUtils = appUtils;
    }

    @Override
    public EntityDto addEntity(EntityDto entityDto) {

        if(entityRepository.existsByEntityTypeIgnoreCase(entityDto.getEntityType().trim()))
            throw new DuplicateEntryException("Entity type '"+entityDto.getEntityType()+"' already exist!");

        EntityTbl entity = new EntityTbl();
        entity.setEntityType(entityDto.getEntityType().trim());
        entity.setShortName(entityDto.getShortName().trim());

        return appUtils.entityToDto(entityRepository.save(entity));
    }

    @Override
    public EntityDto getEntityById(int entityId) {
        return appUtils.entityToDto(entityRepository.findById(entityId)
                .orElseThrow(()->new ResourceNotFoundException("Entity with ID '"+entityId+"' not found!")));
    }

    @Override
    public HttpResponse getAllEntities(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<EntityTbl> pageEntity = entityRepository.findAll(pageable);
        List<EntityDto> entities = appUtils.entitiesToDtos(pageEntity.getContent());

        return HttpResponse.builder()
                .pageNumber(pageEntity.getNumber())
                .pageSize(pageEntity.getSize())
                .totalElements(pageEntity.getTotalElements())
                .totalPages(pageEntity.getTotalPages())
                .isLastPage(pageEntity.isLast())
                .data(entities)
                .build();
    }

    @Override
    public HttpResponse searchEntityByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Integer integerKeyword = null;

        try {
            integerKeyword = Integer.parseInt(keyword);
        }catch (Exception e){

        }

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<EntityTbl> pageEntity = entityRepository.findAllByEntityIdOrEntityTypeContainingIgnoreCaseOrShortNameContainingIgnoreCase
                (integerKeyword, keyword, keyword, pageable);
        List<EntityDto> entities = appUtils.entitiesToDtos(pageEntity.getContent());

        return HttpResponse.builder()
                .pageNumber(pageEntity.getNumber())
                .pageSize(pageEntity.getSize())
                .totalElements(pageEntity.getTotalElements())
                .totalPages(pageEntity.getTotalPages())
                .isLastPage(pageEntity.isLast())
                .data(entities)
                .build();
    }

    @Override
    public EntityDto updateEntity(EntityUpdateDto entityDto, int entityId) {

        EntityTbl entity = entityRepository.findById(entityId)
                .orElseThrow(()->new ResourceNotFoundException("Entity with ID '"+entityId+"' not found!"));

        if(entityDto.getEntityType() != null)
        {
            EntityTbl entityWithSameName = entityRepository.findByEntityTypeIgnoreCase(entityDto.getEntityType().trim());
            if(entityWithSameName != null && !entityWithSameName.getEntityId().equals(entity.getEntityId()))
                throw new DuplicateEntryException("Entity '"+entityDto.getEntityType()+"' already exist!");

            entity.setEntityType(entityDto.getEntityType().trim());
        }

        if(entityDto.getShortName() != null)
            entity.setShortName(entityDto.getShortName().trim());

        return appUtils.entityToDto(entityRepository.save(entity));
    }

    @Override
    public String deleteEntity(int entityId) {
        EntityTbl entity = entityRepository.findById(entityId)
                .orElseThrow(()->new ResourceNotFoundException("Entity with ID '"+entityId+"' not found!"));
        entityRepository.delete(entity);
        return "Entity '"+entity.getEntityType()+"' deleted!";
    }
}
