package com.vaistra.repositories;

import com.vaistra.entities.Country;
import com.vaistra.entities.District;
import com.vaistra.entities.State;
import com.vaistra.entities.SubDistrict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDistrictRepository extends JpaRepository<SubDistrict, Integer> {

    Page<SubDistrict> findAllByDistrict(District district, Pageable p);
    Page<SubDistrict> findAllByDistrict_State(State state, Pageable p);
    Page<SubDistrict> findAllByDistrict_State_Country(Country country, Pageable p);

    Page<SubDistrict> findAllByDistrict_Status(boolean b, Pageable p);

    boolean existsBySubDistrictName(String name);

    Page<SubDistrict> findAllBySubDistrictNameContainingIgnoreCase(String name, Pageable p);
}
