package com.vaistra.services.mastermines.impl;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.DesignationDto;
import com.vaistra.dto.mastermines.MineralDto;
import com.vaistra.entities.mastermines.Designation;
import com.vaistra.entities.mastermines.Mineral;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.mastermines.DesignationRepository;
import com.vaistra.services.mastermines.DesignationService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepository designationRepository;
    private final AppUtils appUtils;

    @Autowired
    public DesignationServiceImpl(DesignationRepository designationRepository, AppUtils appUtils) {
        this.designationRepository = designationRepository;
        this.appUtils = appUtils;
    }

    @Override
    public DesignationDto addDesignation(DesignationDto designationDto) {

        if(designationRepository.existsByDesignationTypeIgnoreCase(designationDto.getDesignationType().trim()))
            throw new DuplicateEntryException("Designation type '"+designationDto.getDesignationType()+"' already exist");

        Designation designation = new Designation();
        designation.setDesignationType(designationDto.getDesignationType().trim());

        return appUtils.designationToDto(designationRepository.save(designation));
    }

    @Override
    public DesignationDto getDesignationById(int designationId) {
        return appUtils.designationToDto(designationRepository.findById(designationId)
                .orElseThrow(()->new ResourceNotFoundException("Designation with ID '"+designationId+"' not found!")));
    }

    @Override
    public HttpResponse getAllDesignations(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Designation> pageDesignation = designationRepository.findAll(pageable);
        List<DesignationDto> designations = appUtils.designationsToDto(pageDesignation.getContent());

        return HttpResponse.builder()
                .pageNumber(pageDesignation.getNumber())
                .pageSize(pageDesignation.getSize())
                .totalElements(pageDesignation.getTotalElements())
                .totalPages(pageDesignation.getTotalPages())
                .isLastPage(pageDesignation.isLast())
                .data(designations)
                .build();
    }

    @Override
    public HttpResponse searchDesignationsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Integer integerKeyword = null;

        try {
            integerKeyword = Integer.parseInt(keyword);

        }catch (Exception e){

        }

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Designation> pageDesignation = designationRepository.findAllByDesignationIdOrDesignationTypeContainingIgnoreCase(integerKeyword, keyword, pageable);
        List<DesignationDto> designations = appUtils.designationsToDto(pageDesignation.getContent());

        return HttpResponse.builder()
                .pageNumber(pageDesignation.getNumber())
                .pageSize(pageDesignation.getSize())
                .totalElements(pageDesignation.getTotalElements())
                .totalPages(pageDesignation.getTotalPages())
                .isLastPage(pageDesignation.isLast())
                .data(designations)
                .build();
    }

    @Override
    public DesignationDto updateDesignation(DesignationDto designationDto, int designationId) {

        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(()->new ResourceNotFoundException("Designation with ID '"+designationId+"' not found!"));

        if(designationDto.getDesignationType() != null)
        {
            Designation designationWithSameName = designationRepository.findByDesignationTypeIgnoreCase(designationDto.getDesignationType().trim());
            if(designationWithSameName != null && !designationWithSameName.getDesignationId().equals(designation.getDesignationId()))
                throw new DuplicateEntryException("Designation '"+designationDto.getDesignationType()+"' already exist!");

            designation.setDesignationType(designationDto.getDesignationType().trim());
        }

        return appUtils.designationToDto(designationRepository.save(designation));
    }

    @Override
    public String deleteDesignation(int designationId) {
        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(()->new ResourceNotFoundException("Designation with ID '"+designationId+"' not found!"));
        return "Designation '"+designation.getDesignationType()+"' deleted!";
    }
}
