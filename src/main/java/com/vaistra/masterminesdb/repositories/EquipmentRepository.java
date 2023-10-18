package com.vaistra.masterminesdb.repositories;

import com.vaistra.masterminesdb.entities.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    Boolean existsByEquipmentNameIgnoreCase(String equipmentName);
    Equipment findByEquipmentNameIgnoreCase(String name);
    Page<Equipment> findAllByEquipmentIdOrEquipmentNameContainingIgnoreCase(Integer equipmentId, String equipmentName, Pageable p);
}
