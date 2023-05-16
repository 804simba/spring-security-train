package com.simba.security.app.utils;

import com.simba.security.app.model.dto.RegisterDTO;
import com.simba.security.app.model.entity.User;

public class EntityMapper {
    public static User toEntity(RegisterDTO registerDTO) {
        return User.builder()
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .email(registerDTO.getEmail())
                .build();
    }
}
