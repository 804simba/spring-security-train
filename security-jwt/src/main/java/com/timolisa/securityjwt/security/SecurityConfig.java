package com.timolisa.securityjwt.security;

import com.timolisa.securityjwt.filter.CustomAuthenticationFilter;
import com.timolisa.securityjwt.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    // https://stackoverflow.com/questions/73378676/spring-boot-custom-authentication-filter-without-using-the-websecurityconfigur
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // override /login
        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManagerBuilder.getOrBuild());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
         http
                 .csrf()
                 .disable()
                 .sessionManagement()
                 .sessionCreationPolicy(STATELESS)
                 .and().authorizeHttpRequests().requestMatchers("/api/login/**", "/api/token/refresh/**").permitAll()
                 .and().authorizeHttpRequests().requestMatchers(GET, "/api/users/**").hasAnyAuthority("ROLE_USER")
                 .and().authorizeHttpRequests().requestMatchers(POST, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN")
                 .and().authorizeHttpRequests().anyRequest().authenticated();
         http.addFilter(customAuthenticationFilter);
         http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    public void authenticationManager(AuthenticationConfiguration auth) throws Exception {
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(bCryptPasswordEncoder);
//    }
}
