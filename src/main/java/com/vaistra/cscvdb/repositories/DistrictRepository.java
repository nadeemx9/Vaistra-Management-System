package com.vaistra.cscvdb.repositories;

import com.vaistra.cscvdb.entities.Country;
import com.vaistra.cscvdb.entities.District;
import com.vaistra.cscvdb.entities.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    Page<District> findAllByState(State state, Pageable p);
    Page<District> findAllByCountry(Country country, Pageable p);

    District findByDistrictNameIgnoreCase(String name);
    boolean existsByDistrictNameIgnoreCase(String name);
    Page<District> findAllByState_Status(Boolean b, Pageable p);

    Page<District> findAllByDistrictIdOrStatusOrDistrictNameContainingIgnoreCaseOrState_StateNameContainingIgnoreCaseOrCountry_CountryNameContainingIgnoreCase
            (Integer districtId, Boolean status, String districtName, String stateName, String countryName, Pageable p);
}
