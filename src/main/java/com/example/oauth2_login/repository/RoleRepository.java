package com.example.oauth2_login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oauth2_login.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	Optional<Role> findByName(Role.RoleType name);
}
