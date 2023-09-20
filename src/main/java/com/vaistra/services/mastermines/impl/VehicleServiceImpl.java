package com.vaistra.services.mastermines.impl;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.EquipmentDto;
import com.vaistra.dto.mastermines.VehicleDto;
import com.vaistra.dto.mastermines.VehicleUpdateDto;
import com.vaistra.entities.mastermines.Equipment;
import com.vaistra.entities.mastermines.Vehicle;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.mastermines.VehicleRepository;
import com.vaistra.services.mastermines.VehicleService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final AppUtils appUtils;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository, AppUtils appUtils) {
        this.vehicleRepository = vehicleRepository;
        this.appUtils = appUtils;
    }

    @Override
    public VehicleDto addVehicle(VehicleDto vehicleDto) {

        if(vehicleRepository.existsByVehicleNameIgnoreCase(vehicleDto.getVehicleName().trim()))
            throw new DuplicateEntryException("Vehicle with name '"+vehicleDto.getVehicleName()+"' already exits!");

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleName(vehicleDto.getVehicleName().trim());


        return appUtils.vehicleToDto(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleDto getVehicleById(int vehicleId) {
        return appUtils.vehicleToDto(vehicleRepository.findById(vehicleId)
                .orElseThrow(()->new ResourceNotFoundException("Vehicle with ID '"+vehicleId+"' not found!")));
    }

    @Override
    public HttpResponse getAllVehicles(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Vehicle> pageVehicle = vehicleRepository.findAll(pageable);
        List<VehicleDto> vehicles = appUtils.vehiclesToDtos(pageVehicle.getContent());

        return HttpResponse.builder()
                .pageNumber(pageVehicle.getNumber())
                .pageSize(pageVehicle.getSize())
                .totalElements(pageVehicle.getTotalElements())
                .totalPages(pageVehicle.getTotalPages())
                .isLastPage(pageVehicle.isLast())
                .data(vehicles)
                .build();
    }

    @Override
    public HttpResponse searchVehicleByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Integer integerKeyword = null;

        try {
            integerKeyword = Integer.parseInt(keyword);

        }catch (Exception e){

        }

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Vehicle> pageVehicle = vehicleRepository.findAllByVehicleIdOrVehicleNameContainingIgnoreCase(integerKeyword, keyword, pageable);
        List<VehicleDto> vehicles = appUtils.vehiclesToDtos(pageVehicle.getContent());

        return HttpResponse.builder()
                .pageNumber(pageVehicle.getNumber())
                .pageSize(pageVehicle.getSize())
                .totalElements(pageVehicle.getTotalElements())
                .totalPages(pageVehicle.getTotalPages())
                .isLastPage(pageVehicle.isLast())
                .data(vehicles)
                .build();
    }

    @Override
    public VehicleDto updateVehicle(VehicleUpdateDto vehicleDto, int vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(()->new ResourceNotFoundException("Vehicle with ID '"+vehicleId+"' not found!"));

        Vehicle vehicleWithSameName = vehicleRepository.findByVehicleNameIgnoreCase(vehicleDto.getVehicleName().trim());
        if(vehicleWithSameName != null && !vehicleWithSameName.getVehicleId().equals(vehicle.getVehicleId()))
            throw new DuplicateEntryException("Vehicle '"+vehicleDto.getVehicleName()+"' already exist!");

        vehicle.setVehicleName(vehicleDto.getVehicleName().trim());

        return appUtils.vehicleToDto(vehicleRepository.save(vehicle));
    }

    @Override
    public String deleteVehicle(int vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(()->new ResourceNotFoundException("Vehicle with ID '"+vehicleId+"' not found!"));

        vehicleRepository.delete(vehicle);
        return "Vehicle with ID '"+vehicleId+"' not found!";
    }
}
