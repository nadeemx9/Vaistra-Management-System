package com.vaistra.repositories.cscv;

import com.vaistra.entities.cscv.Country;
import com.vaistra.entities.cscv.State;
import com.vaistra.entities.cscv.SubDistrict;
import com.vaistra.entities.cscv.Village;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VillageRepository extends JpaRepository<Village, Integer> {

    Page<Village> findAllBySubDistrict(SubDistrict subDistrict, Pageable p);
    Page<Village> findAllBySubDistrict_District_State(State state, Pageable p);
    Page<Village> findAllBySubDistrict_District_State_Country(Country country, Pageable p);
    Page<Village> findAllBySubDistrict_Status(boolean b, Pageable p);
    boolean existsByVillageName(String name);
    Village findByVillageNameIgnoreCase(String name);
    Page<Village> findAllByVillageIdOrStatusOrVillageNameContainingIgnoreCaseOrSubDistrict_SubDistrictNameContainingIgnoreCaseOrDistrict_DistrictNameContainingIgnoreCaseOrState_StateNameContainingIgnoreCaseOrCountry_CountryNameContainingIgnoreCase
            (Integer villageId, Boolean status, String villageName, String subDistrictName, String districtName, String stateName, String countryName, Pageable p);
}
