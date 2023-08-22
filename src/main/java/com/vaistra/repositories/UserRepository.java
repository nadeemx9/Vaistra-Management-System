package com.vaistra.repositories;

import com.vaistra.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String username);
    Boolean existsByEmail(String email);
    User findByEmailIgnoreCase(String email);

    List<User> findAllByDeletedIs(Boolean b);
    List<User> findAllByDeleted(Boolean b);
}
