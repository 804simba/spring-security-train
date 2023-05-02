package com.timolisa.securityjwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final String SECRET = "secret";
    private static final long ACCESS_TOKEN_VALIDITY = 10 * 60 * 1000;
    private static final long REFRESH_TOKEN_VALIDITY = 30 * 60 * 1000;

    public static String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities, String issuer) {
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(algorithm);
    }

    public static String generateRefreshToken(String username, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .withIssuer(issuer)
                .sign(algorithm);
    }
}

