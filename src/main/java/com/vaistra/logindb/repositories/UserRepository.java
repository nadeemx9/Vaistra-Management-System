package com.vaistra.logindb.repositories;

import com.vaistra.logindb.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByEmailIgnoreCase(String email);
    User findByEmailIgnoreCase(String email);

}
