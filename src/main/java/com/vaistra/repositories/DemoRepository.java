package com.vaistra.repositories;

import com.vaistra.entities.DemoCSV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoRepository extends JpaRepository<DemoCSV,Integer> {

}