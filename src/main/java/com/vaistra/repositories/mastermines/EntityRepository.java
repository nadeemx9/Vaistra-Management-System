package com.vaistra.repositories.mastermines;

import com.vaistra.entities.mastermines.EntityTbl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityRepository extends JpaRepository<EntityTbl, Integer> {
}
