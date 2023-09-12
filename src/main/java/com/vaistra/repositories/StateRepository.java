package com.vaistra.repositories;

import com.vaistra.entities.Country;
import com.vaistra.entities.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
    Page<State> findAllByCountry(Country country, Pageable p);
    State findByStateName(String name);
    boolean existsByStateName(String name);
    Page<State> findAllByCountry_Status(boolean b, Pageable p);
    Page<State> findAllByStateNameContainingIgnoreCase(String keyword, Pageable p);

}
