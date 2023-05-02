package com.timolisa.securityjwt.api;

import lombok.Data;

@Data
public class RoleToUserForm {
    private String userName;
    private String roleName;
}
