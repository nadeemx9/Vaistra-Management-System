package com.vaistra.repositories.mastermines;

import com.vaistra.entities.mastermines.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
}
