package com.vaistra.cscvdb.repositories;

import com.vaistra.cscvdb.entities.Country;
import com.vaistra.cscvdb.entities.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
    Page<State> findAllByCountry(Country country, Pageable p);
    State findByStateNameIgnoreCase(String name);
    boolean existsByStateName(String name);
    Page<State> findAllByCountry_Status(boolean b, Pageable p);
    Page<State> findAllByStateIdOrStatusOrStateNameContainingIgnoreCaseOrCountry_CountryNameContainingIgnoreCase(Integer stateId, Boolean status ,String stateName, String countryName, Pageable p);

    boolean existsByStateNameIgnoreCase(String sname);
}
