package com.simba.security.app.model.dto;

import com.simba.security.app.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RegisterDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;
}
