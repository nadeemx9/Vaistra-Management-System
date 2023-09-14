package com.vaistra.repositories.mastermines;

import com.vaistra.entities.mastermines.Equipment;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    Boolean existsByEquipmentNameIgnoreCase(String equipmentName);

    Page<Equipment> findAllByEquipmentIdOrEquipmentNameContainingIgnoreCase(Integer equipmentId, String equipmentName, Pageable p);
}
