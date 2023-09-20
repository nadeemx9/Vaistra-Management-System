package com.vaistra.services.mastermines.impl;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.DesignationDto;
import com.vaistra.dto.mastermines.EquipmentDto;
import com.vaistra.dto.mastermines.EquipmentUpdateDto;
import com.vaistra.entities.mastermines.Designation;
import com.vaistra.entities.mastermines.Equipment;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.mastermines.EquipmentRepository;
import com.vaistra.services.mastermines.EquipmentService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final AppUtils appUtils;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, AppUtils appUtils) {
        this.equipmentRepository = equipmentRepository;
        this.appUtils = appUtils;
    }


    @Override
    public EquipmentDto addEquipment(EquipmentDto equipmentDto) {

        if(equipmentRepository.existsByEquipmentNameIgnoreCase(equipmentDto.getEquipmentName().trim()))
            throw new DuplicateEntryException("Equipment '"+equipmentDto.getEquipmentName()+"' already exist");

        Equipment equipment = new Equipment();
        equipment.setEquipmentName(equipmentDto.getEquipmentName().trim());


        return appUtils.equipmentToDto(equipmentRepository.save(equipment));
    }

    @Override
    public EquipmentDto getEquipmentById(int equipmentId) {
        return appUtils.equipmentToDto(equipmentRepository.findById(equipmentId)
                .orElseThrow(()->new ResourceNotFoundException("Equipment with ID '"+equipmentId+"' not found!")));
    }

    @Override
    public HttpResponse getAllEquipments(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Equipment> pageEquipment = equipmentRepository.findAll(pageable);
        List<EquipmentDto> equipments = appUtils.equipmentsToDtos(pageEquipment.getContent());

        return HttpResponse.builder()
                .pageNumber(pageEquipment.getNumber())
                .pageSize(pageEquipment.getSize())
                .totalElements(pageEquipment.getTotalElements())
                .totalPages(pageEquipment.getTotalPages())
                .isLastPage(pageEquipment.isLast())
                .data(equipments)
                .build();
    }

    @Override
    public HttpResponse searchEquipmentsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Integer integerKeyword = null;

        try {
            integerKeyword = Integer.parseInt(keyword);

        }catch (Exception e){

        }

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Equipment> pageEquipment = equipmentRepository.findAllByEquipmentIdOrEquipmentNameContainingIgnoreCase(integerKeyword, keyword, pageable);
        List<EquipmentDto> equipments = appUtils.equipmentsToDtos(pageEquipment.getContent());

        return HttpResponse.builder()
                .pageNumber(pageEquipment.getNumber())
                .pageSize(pageEquipment.getSize())
                .totalElements(pageEquipment.getTotalElements())
                .totalPages(pageEquipment.getTotalPages())
                .isLastPage(pageEquipment.isLast())
                .data(equipments)
                .build();
    }

    @Override
    public EquipmentDto updateEquipment(EquipmentUpdateDto equipmentDto, int equipmentId) {

        if(equipmentDto.getEquipmentName() == null)
            throw new ResourceNotFoundException("Equipment name is null!");

        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()->new ResourceNotFoundException("Equipment with ID '"+equipmentId+"' not found!"));

       Equipment equipmentWithSameName = equipmentRepository.findByEquipmentNameIgnoreCase(equipmentDto.getEquipmentName().trim());
        if(equipmentWithSameName != null && !equipmentWithSameName.getEquipmentId().equals(equipment.getEquipmentId()))
            throw new DuplicateEntryException("Equipment '"+equipmentDto.getEquipmentName()+"' already exist!");

        equipment.setEquipmentName(equipmentDto.getEquipmentName().trim());

        return appUtils.equipmentToDto(equipmentRepository.save(equipment));
    }

    @Override
    public String deleteEquipment(int equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()->new ResourceNotFoundException("Equipment with ID '"+equipmentId+"' not found!"));

        equipmentRepository.delete(equipment);
        return "Equipment '"+ equipment.getEquipmentName()+"' deleted!";
    }
}
