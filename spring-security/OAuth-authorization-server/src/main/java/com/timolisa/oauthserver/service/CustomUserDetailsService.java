package com.timolisa.oauthserver.service;

import com.timolisa.oauthserver.entity.User;
import com.timolisa.oauthserver.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
// this service is used to validate the user
// this is where the authority mapping takes place
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    // https://www.baeldung.com/spring-security-granted-authority-vs-role

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                getAuthorities(List.of(user.getRole()))
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> grantedAuthorities =
                new ArrayList<>();

        for (String role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return grantedAuthorities;
    }
    /**
     * private Collection<? extends GrantedAuthority> getAuthorities(
     *   Collection<Role> roles) {
     *     List<GrantedAuthority> authorities
     *       = new ArrayList<>();
     *     for (Role role: roles) {
     *         authorities.add(new SimpleGrantedAuthority(role.getName()));
     *         role.getPrivileges().stream()
     *          .map(p -> new SimpleGrantedAuthority(p.getName()))
     *          .forEach(authorities::add);
     *     }
     *
     *     return authorities;
     * }
     * */
}
