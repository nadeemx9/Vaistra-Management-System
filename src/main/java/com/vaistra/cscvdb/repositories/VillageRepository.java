package com.vaistra.cscvdb.repositories;

import com.vaistra.cscvdb.entities.Country;
import com.vaistra.cscvdb.entities.State;
import com.vaistra.cscvdb.entities.SubDistrict;
import com.vaistra.cscvdb.entities.Village;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VillageRepository extends JpaRepository<Village, Integer> {

    Page<Village> findAllBySubDistrict(SubDistrict subDistrict, Pageable p);
    Page<Village> findAllByState(State state, Pageable p);
    Page<Village> findAllByCountry(Country country, Pageable p);
    Page<Village> findAllBySubDistrict_Status(boolean b, Pageable p);
    boolean existsByVillageName(String name);
    Village findByVillageNameIgnoreCase(String name);
    Page<Village> findAllByVillageIdOrStatusOrVillageNameContainingIgnoreCaseOrSubDistrict_SubDistrictNameContainingIgnoreCaseOrDistrict_DistrictNameContainingIgnoreCaseOrState_StateNameContainingIgnoreCaseOrCountry_CountryNameContainingIgnoreCase
            (Integer villageId, Boolean status, String villageName, String subDistrictName, String districtName, String stateName, String countryName, Pageable p);
}
