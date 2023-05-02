package org.timolisa.springsecurityclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    private static final String[] WHITE_LIST_URLS = {
            "/hello",
            "/register",
            "/reset-password*",
            "/save-password*",
            "/change-password*",
            "/resendVerificationToken*",
            "/verifyRegistration*"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        final int STRENGTH = 11;
        return new BCryptPasswordEncoder(STRENGTH);
    }

    // whitelist urls that we want to bypass
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(WHITE_LIST_URLS).permitAll()
                .requestMatchers("/api/**").authenticated()
                .and()
                .oauth2Login(oauth2Login ->
                        oauth2Login.loginPage("/oauth2/authorization/api-client-oidc"))
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }
}
