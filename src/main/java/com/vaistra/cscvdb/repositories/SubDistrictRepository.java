package com.vaistra.cscvdb.repositories;

import com.vaistra.cscvdb.entities.Country;
import com.vaistra.cscvdb.entities.District;
import com.vaistra.cscvdb.entities.State;
import com.vaistra.cscvdb.entities.SubDistrict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDistrictRepository extends JpaRepository<SubDistrict, Integer> {

    Page<SubDistrict> findAllByDistrict(District district, Pageable p);
    Page<SubDistrict> findAllByState(State state, Pageable p);
    Page<SubDistrict> findAllByCountry(Country country, Pageable p);

    SubDistrict findBySubDistrictNameIgnoreCase(String name);
    Page<SubDistrict> findAllByDistrict_Status(boolean b, Pageable p);

    boolean existsBySubDistrictName(String name);

    Page<SubDistrict> findAllBySubDistrictIdOrStatusOrSubDistrictNameContainingIgnoreCaseOrDistrict_DistrictNameContainingIgnoreCaseOrState_StateNameContainingIgnoreCaseOrCountry_CountryNameContainingIgnoreCase
            (Integer subDistrictId, Boolean status, String subDistrictName, String districtName, String stateName, String countryName, Pageable p);
}
