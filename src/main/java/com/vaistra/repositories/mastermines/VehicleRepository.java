package com.vaistra.repositories.mastermines;

import com.vaistra.entities.mastermines.Vehicle;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    Boolean existsByVehicleNameIgnoreCase(String equipmentName);
    Vehicle findByVehicleNameIgnoreCase(String name);
    Page<Vehicle> findAllByVehicleIdOrVehicleNameContainingIgnoreCase(Integer vehicleId, String vehicleName, Pageable p);
}
