package com.vaistra.repositories;

import com.vaistra.entities.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Integer> {
    Confirmation findByToken(String token);
}
