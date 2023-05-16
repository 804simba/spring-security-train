package com.simba.security.app.repository;

import com.simba.security.app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
