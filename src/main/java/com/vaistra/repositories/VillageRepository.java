package com.vaistra.repositories;

import com.vaistra.entities.Country;
import com.vaistra.entities.State;
import com.vaistra.entities.SubDistrict;
import com.vaistra.entities.Village;
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
    Page<Village> findAllByVillageNameContainingIgnoreCase(String name, Pageable p);
}
