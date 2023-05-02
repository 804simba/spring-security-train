package org.timolisa.springsecurityclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.timolisa.springsecurityclient.entity.VerificationToken;

@Repository
public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
