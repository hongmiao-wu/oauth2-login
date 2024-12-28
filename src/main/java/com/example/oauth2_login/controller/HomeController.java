package com.example.oauth2_login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	// TODO: add role-based endpoints
	
	@GetMapping("/")
	public String home()
	{
		return "Hello home!";
	}
	
	@GetMapping("/secured")
	public String secured()
	{
		return "Hello secured!";
	}
	
	@GetMapping("/user")
	public String user()
	{
		return "Hello user!";
	}

}
