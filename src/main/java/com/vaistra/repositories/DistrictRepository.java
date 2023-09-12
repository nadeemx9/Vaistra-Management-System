package com.vaistra.repositories;

import com.vaistra.entities.Country;
import com.vaistra.entities.District;
import com.vaistra.entities.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    Page<District> findAllByState(State state, Pageable p);
    Page<District> findAllByState_Country(Country country, Pageable p);

    boolean existsByDistrictName(String name);
    Page<District> findAllByState_Status(Boolean b, Pageable p);

    Page<District> findAllByDistrictNameContainingIgnoreCase(String keyword, Pageable p);
}
