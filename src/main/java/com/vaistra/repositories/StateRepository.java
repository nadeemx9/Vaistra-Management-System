package com.vaistra.repositories;

import com.vaistra.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
    List<State> findByCountry_CountryId(int countryId);
    State findByStateName(String name);
}
