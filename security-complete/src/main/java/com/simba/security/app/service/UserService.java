package com.simba.security.app.service;

import com.simba.security.app.model.dto.AuthenticationRequest;
import com.simba.security.app.model.dto.AuthenticationResponse;
import com.simba.security.app.model.dto.RegisterDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

public interface UserService {
    UserDetails loadUserByUsername(String email);

    AuthenticationResponse register(RegisterDTO registerDTO);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
