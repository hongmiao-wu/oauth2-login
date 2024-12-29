package com.example.oauth2_login;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.oauth2_login.model.Role;
import com.example.oauth2_login.model.User;
import com.example.oauth2_login.repository.RoleRepository;
import com.example.oauth2_login.repository.UserRepository;
import com.example.oauth2_login.service.UserService;

@SpringBootApplication
public class Oauth2LoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2LoginApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run (UserService userService, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder)
	{
		return args ->
		{
			Optional<User> existingAdmin = userRepository.findByEmail("admin@gmail.com");
			if (existingAdmin.isEmpty())
			{
				Role adminRole = roleRepository.findByName(Role.RoleType.ROLE_ADMIN).get();
				Role userRole = roleRepository.findByName(Role.RoleType.ROLE_USER).get();
				Set<Role> adminRoles = new HashSet<>();
				adminRoles.add(adminRole);
				adminRoles.add(userRole);
				User adminUser = new User("adminName", "admin@gmail.com", passwordEncoder.encode("adminPassword"), adminRoles);
				userService.saveUser(adminUser);
			}
			else
			{
				System.out.println("Admin user already exists!");
			}
			
		};
	}
}
