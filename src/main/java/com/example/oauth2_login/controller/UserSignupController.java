package com.example.oauth2_login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2_login.model.UserSignupDTO;
import com.example.oauth2_login.service.UserService;

@RestController
public class UserSignupController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserSignupDTO userSignupDTO) {
        try {
            userService.registerUser(userSignupDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

}
