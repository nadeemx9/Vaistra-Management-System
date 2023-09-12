package com.vaistra.repositories;

import com.vaistra.entities.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Country findByCountryName(String name);
    Page<Country> findAllByStatus(Boolean b, Pageable p);
    Page<Country> findByCountryNameContainingIgnoreCase(String keyword, Pageable p);
    boolean existsByCountryName(String name);
}
