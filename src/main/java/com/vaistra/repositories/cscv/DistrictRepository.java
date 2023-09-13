package com.vaistra.repositories.cscv;

import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.District;
import com.vaistra.entities.cscv.State;
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

    Page<District> findAllByDistrictIdOrStatusOrDistrictNameContainingIgnoreCaseOrState_StateNameContainingIgnoreCaseOrCountry_CountryNameContainingIgnoreCase
            (Integer districtId, Boolean status, String districtName, String stateName, String countryName, Pageable p);
}
