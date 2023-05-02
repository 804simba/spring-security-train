package org.timolisa.springsecurityclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.timolisa.springsecurityclient.entity.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
