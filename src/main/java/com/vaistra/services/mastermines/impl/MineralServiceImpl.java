package com.vaistra.services.mastermines.impl;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.MineralDto;
import com.vaistra.entities.mastermines.Mineral;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.mastermines.MineralRepository;
import com.vaistra.services.mastermines.MineralService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MineralServiceImpl implements MineralService {

    private final MineralRepository mineralRepository;
    private final AppUtils appUtils;

    @Autowired
    public MineralServiceImpl(MineralRepository mineralRepository, AppUtils appUtils) {
        this.mineralRepository = mineralRepository;
        this.appUtils = appUtils;
    }

    @Override
    public MineralDto addMineral(MineralDto mineralDto) {

        if(mineralRepository.existsByMineralNameIgnoreCase(mineralDto.getMineralName().trim()))
            throw new DuplicateEntryException("Mineral with name '"+mineralDto.getMineralName()+"' already exist!");

        if(mineralRepository.existsByAtrNameIgnoreCase(mineralDto.getAtrName().trim()))
            throw new DuplicateEntryException("ATR name '"+mineralDto.getAtrName()+"' already exist");

        if(mineralRepository.existsByHsnCodeIgnoreCase(mineralDto.getHsnCode().trim()))
            throw new ResourceNotFoundException("HSN code '"+mineralDto.getHsnCode()+"' already exist!");

        Mineral mineral = new Mineral();
        mineral.setMineralName(mineralDto.getMineralName().trim());
        mineral.setCategory(mineralDto.getCategory().trim());
        mineral.setAtrName(mineralDto.getAtrName().trim());
        mineral.setHsnCode(mineralDto.getHsnCode().trim());
        mineral.setGrade(Arrays.asList(mineralDto.getGrade()));

        return appUtils.mineralToDto(mineralRepository.save(mineral));
    }

    @Override
    public MineralDto getMineralById(int mineralId) {
        return appUtils.mineralToDto(mineralRepository.findById(mineralId)
                .orElseThrow(()->new ResourceNotFoundException("Mineral with ID '"+mineralId+"' not found!")));
    }

    @Override
    public HttpResponse getAllMinerals(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Mineral> pageMineral = mineralRepository.findAll(pageable);
        List<MineralDto> minerals = appUtils.mineralsToDtos(pageMineral.getContent());

        return HttpResponse.builder()
                .pageNumber(pageMineral.getNumber())
                .pageSize(pageMineral.getSize())
                .totalElements(pageMineral.getTotalElements())
                .totalPages(pageMineral.getTotalPages())
                .isLastPage(pageMineral.isLast())
                .data(minerals)
                .build();
    }

    @Override
    public HttpResponse searchMineralByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Integer integerKeyword = null;

        try {
            integerKeyword = Integer.parseInt(keyword);

        }catch (Exception e){

        }

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Mineral> pageMineral = mineralRepository.
                findAllByMineralIdOrMineralNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrAtrNameContainingIgnoreCaseOrHsnCodeContainingIgnoreCase
                        (integerKeyword, keyword, keyword, keyword, keyword, pageable);
        List<MineralDto> minerals = appUtils.mineralsToDtos(pageMineral.getContent());

        return HttpResponse.builder()
                .pageNumber(pageMineral.getNumber())
                .pageSize(pageMineral.getSize())
                .totalElements(pageMineral.getTotalElements())
                .totalPages(pageMineral.getTotalPages())
                .isLastPage(pageMineral.isLast())
                .data(minerals)
                .build();
    }

    @Override
    public MineralDto updateMineral(MineralDto mineralDto, int mineralId) {

        Mineral mineral = mineralRepository.findById(mineralId)
                .orElseThrow(()->new ResourceNotFoundException("Mineral with ID '"+mineralId+"' not found!"));


        if(mineralDto.getMineralName() != null) {
            if (mineralRepository.existsByMineralNameIgnoreCase(mineralDto.getMineralName().trim()))
                throw new DuplicateEntryException("Mineral with name '" + mineralDto.getMineralName() + "' already exist!");

            mineral.setMineralName(mineralDto.getMineralName().trim());
        }

        if(mineralDto.getAtrName() != null) {
            if(mineralRepository.existsByAtrNameIgnoreCase(mineralDto.getAtrName().trim()))
                throw new DuplicateEntryException("ATR name '"+mineralDto.getAtrName()+"' already exist");

            mineral.setAtrName(mineralDto.getAtrName().trim());
        }

        if(mineralDto.getHsnCode() != null) {
            if(mineralRepository.existsByHsnCodeIgnoreCase(mineralDto.getHsnCode().trim()))
                throw new ResourceNotFoundException("HSN code '"+mineralDto.getHsnCode()+"' already exist!");

            mineral.setHsnCode(mineralDto.getHsnCode().trim());
        }

        if(mineralDto.getCategory() != null)
            mineral.setCategory(mineralDto.getCategory().trim());

        try {
            if(mineralDto.getGrade().length != 0)
                mineral.setGrade(Arrays.asList(mineralDto.getGrade()));
        }catch (Exception e){

        }

        return appUtils.mineralToDto(mineralRepository.save(mineral));
    }

    @Override
    public String deleteMineral(int mineralId) {
        Mineral mineral = mineralRepository.findById(mineralId)
                .orElseThrow(()->new ResourceNotFoundException("Mineral with ID '"+mineralId+"' not found!"));

        return "Mineral '"+mineral.getMineralName()+"' deleted!";
    }
}
