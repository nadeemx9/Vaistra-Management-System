package com.vaistra.masterminesdb.repositories;

import com.vaistra.masterminesdb.entities.Mineral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MineralRepository extends JpaRepository<Mineral,Integer> {

    Page<Mineral> findAllByMineralIdOrMineralNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrAtrNameContainingIgnoreCaseOrHsnCodeContainingIgnoreCase(Integer mineralId, String mineralName, String category, String atrName, String hsnCode, Pageable p);
    Boolean existsByMineralNameIgnoreCase(String mineralName);
    Boolean existsByAtrNameIgnoreCase(String atrName);
    Boolean existsByHsnCodeIgnoreCase(String hsnCode);
    Mineral findByMineralNameIgnoreCase(String name);
    Mineral findByAtrNameIgnoreCase(String name);
    Mineral findByHsnCodeIgnoreCase(String name);

}
