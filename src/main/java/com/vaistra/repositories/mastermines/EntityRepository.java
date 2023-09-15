package com.vaistra.repositories.mastermines;

import com.vaistra.entities.mastermines.EntityTbl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityRepository extends JpaRepository<EntityTbl, Integer> {

    Page<EntityTbl> findAllByEntityIdOrEntityTypeContainingIgnoreCaseOrShortNameContainingIgnoreCase(Integer entityId, String entityType, String shortName, Pageable p);
    Boolean existsByEntityTypeIgnoreCase(String entityType);

    EntityTbl findByEntityTypeIgnoreCase(String type);
}
