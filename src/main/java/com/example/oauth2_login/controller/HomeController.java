package com.example.oauth2_login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping("/")
	public String home()
	{
//		everyone can see this
		return "Hello home!";
	}
	
	@GetMapping("/secured")
	public String secured()
	{
//		only logged in user can see this
		return "Hello secured!";
	}
	

}
