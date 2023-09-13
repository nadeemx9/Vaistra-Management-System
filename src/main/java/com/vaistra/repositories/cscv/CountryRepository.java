package com.vaistra.repositories.cscv;

import com.vaistra.entities.cscv.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Country findByCountryName(String name);
    Page<Country> findAllByStatus(Boolean b, Pageable p);
    Page<Country> findByCountryIdOrStatusOrCountryNameContainingIgnoreCase(Integer countryId, Boolean status, String keyword, Pageable p);
    boolean existsByCountryName(String name);
}
