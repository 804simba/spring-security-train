package com.simba.security.app.service;

import com.simba.security.app.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        final String authenticationHeader = request.getHeader("Authorization");
        final String token;
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            return;
        }
        token = authenticationHeader.substring(7);

        var storedToken = tokenRepository.findByToken(token)
                .orElse(null);

        if (storedToken != null) {
            storedToken.setRevoked(true);
            storedToken.setExpired(true);
            tokenRepository.save(storedToken);
        }
    }
}
