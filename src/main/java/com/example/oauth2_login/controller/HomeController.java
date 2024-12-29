package com.example.oauth2_login.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping("/")
	public String home()
	{
		return "Hello home!";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole(T(com.example.oauth2_login.model.Role$RoleType).ROLE_USER.name())")
	public String user()
	{
		return "Hello user!";
	}
	
	@GetMapping("/staff")
	@PreAuthorize("hasRole(T(com.example.oauth2_login.model.Role$RoleType).ROLE_STAFF.name())")
	public String staff()
	{
		return "Hello staff!";
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole(T(com.example.oauth2_login.model.Role$RoleType).ROLE_ADMIN.name())")
	public String admin()
	{
		return "Hello admin!";
	}

}
