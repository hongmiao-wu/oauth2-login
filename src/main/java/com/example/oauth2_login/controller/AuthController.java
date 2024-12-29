package com.example.oauth2_login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2_login.model.LoginDTO;
import com.example.oauth2_login.model.UserRegisterDTO;
import com.example.oauth2_login.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	UserService userService;

    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            userService.registerUser(userRegisterDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody LoginDTO loginDTO) {
        return userService.authenticate(loginDTO);
    }
}

