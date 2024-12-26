package com.example.oauth2_login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2_login.config.JwtUtil;

import lombok.Getter;
import lombok.Setter;

@RestController
public class HomeController {
	
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
