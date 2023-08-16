package com.vaistra.repositories;

import com.vaistra.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<District> findByState_StateId(int stateId);
    List<District> findByState_Country_CountryId(int countryId);

    District findByDistrictName(String name);

}
