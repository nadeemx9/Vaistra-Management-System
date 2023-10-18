package com.vaistra.masterminesdb.repositories;

import com.vaistra.masterminesdb.entities.Designation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepository extends JpaRepository<Designation, Integer> {

    Boolean existsByDesignationTypeIgnoreCase(String designationType);
    Designation findByDesignationTypeIgnoreCase(String name);
    Page<Designation> findAllByDesignationIdOrDesignationTypeContainingIgnoreCase(Integer designationId, String designationType, Pageable p);

}
