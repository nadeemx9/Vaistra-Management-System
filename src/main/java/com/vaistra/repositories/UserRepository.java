package com.vaistra.repositories;

import com.vaistra.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Boolean existsByEmailIgnoreCase(String email);
    User findByEmailIgnoreCase(String email);

}
