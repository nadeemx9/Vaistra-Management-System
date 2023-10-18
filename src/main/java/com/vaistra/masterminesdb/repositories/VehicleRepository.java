package com.vaistra.masterminesdb.repositories;

import com.vaistra.masterminesdb.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    Boolean existsByVehicleNameIgnoreCase(String equipmentName);
    Vehicle findByVehicleNameIgnoreCase(String name);
    Page<Vehicle> findAllByVehicleIdOrVehicleNameContainingIgnoreCase(Integer vehicleId, String vehicleName, Pageable p);
}
