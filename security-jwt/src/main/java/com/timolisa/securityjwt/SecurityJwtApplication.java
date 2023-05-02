package com.timolisa.securityjwt;

import com.timolisa.securityjwt.domain.Role;
import com.timolisa.securityjwt.domain.User;
import com.timolisa.securityjwt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityJwtApplication.class, args);
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Bean
	CommandLineRunner runner(UserService userService) {
		return args -> {
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_MANAGER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			userService.saveUser(
					new User(null, "John Doe", "john", "1234"));
			userService.saveUser(
					new User(null, "Mick Jagger", "mike", "1234"));
			userService.saveUser(
					new User(null, "Trey Brown", "trey", "1234"));
			userService.saveUser(
					new User(null, "Vinicius Joe", "vini", "1234"));

			userService.addRoleToUser("john", "ROLE_USER");
			userService.addRoleToUser("john", "ROLE_MANAGER");

			userService.addRoleToUser("mike", "ROLE_MANAGER");

			userService.addRoleToUser("trey", "ROLE_ADMIN");

			userService.addRoleToUser("vini", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("vini", "ROLE_ADMIN");
			userService.addRoleToUser("vini", "ROLE_USER");
		};
	}
}
