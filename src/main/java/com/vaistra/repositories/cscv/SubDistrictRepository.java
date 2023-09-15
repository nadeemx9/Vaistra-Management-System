package com.vaistra.repositories.cscv;

import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.District;
import com.vaistra.entities.cscv.State;
import com.vaistra.entities.cscv.SubDistrict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDistrictRepository extends JpaRepository<SubDistrict, Integer> {

    Page<SubDistrict> findAllByDistrict(District district, Pageable p);
    Page<SubDistrict> findAllByDistrict_State(State state, Pageable p);
    Page<SubDistrict> findAllByDistrict_State_Country(Country country, Pageable p);

    SubDistrict findBySubDistrictNameIgnoreCase(String name);
    Page<SubDistrict> findAllByDistrict_Status(boolean b, Pageable p);

    boolean existsBySubDistrictName(String name);

    Page<SubDistrict> findAllBySubDistrictIdOrStatusOrSubDistrictNameContainingIgnoreCaseOrDistrict_DistrictNameContainingIgnoreCaseOrState_StateNameContainingIgnoreCaseOrCountry_CountryNameContainingIgnoreCase
            (Integer subDistrictId, Boolean status, String subDistrictName, String districtName, String stateName, String countryName, Pageable p);
}
