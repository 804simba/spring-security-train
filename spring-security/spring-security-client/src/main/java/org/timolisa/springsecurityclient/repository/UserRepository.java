package org.timolisa.springsecurityclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.timolisa.springsecurityclient.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
