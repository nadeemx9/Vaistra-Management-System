package com.vaistra.repositories.mastermines;

import com.vaistra.entities.mastermines.Designation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepository extends JpaRepository<Designation, Integer> {

    Boolean existsByDesignationTypeIgnoreCase(String designationType);

    Page<Designation> findAllByDesignationIdOrDesignationTypeContainingIgnoreCase(Integer designationId, String designationType, Pageable p);

}
