package com.vaistra.logindb.repositories;

import com.vaistra.logindb.entities.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Integer> {
    Confirmation findByToken(String token);
}
