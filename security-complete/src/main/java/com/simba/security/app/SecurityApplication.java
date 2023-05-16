package com.simba.security.app;

import com.simba.security.app.model.dto.RegisterDTO;
import com.simba.security.app.model.enums.Role;
import com.simba.security.app.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

//    @Bean
    public CommandLineRunner commandLineRunner(UserService userService) {

        return args -> {
            var admin = RegisterDTO.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .email("admin@gmail.com")
                    .password("password")
                    .role(Role.ADMIN)
                    .build();

            System.out.println("Admin token ::: " + userService.register(admin).getAccessToken());

            var manager = RegisterDTO.builder()
                    .firstName("Manager")
                    .lastName("Manager")
                    .email("manager@gmail.com")
                    .password("password")
                    .role(Role.MANAGER)
                    .build();

            System.out.println("Management token ::: " + userService.register(manager).getAccessToken());
        };
    }
}