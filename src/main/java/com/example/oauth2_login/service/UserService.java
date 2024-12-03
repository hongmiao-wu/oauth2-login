package com.example.oauth2_login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oauth2_login.model.User;
import com.example.oauth2_login.model.UserSignupDTO;
import com.example.oauth2_login.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void registerUser(UserSignupDTO userSignupDTO) {

		if (userSignupDTO.getEmail() == null || userSignupDTO.getPassword() == null || userSignupDTO.getName() == null)
		{
			throw new IllegalArgumentException("User name, email and password are required.");
		}
		
		if (userRepository.findByEmail(userSignupDTO.getEmail()).isPresent())
		{
			throw new IllegalArgumentException("Email is already in use.");
		}
		
		User newUser = new User();
		newUser.setName(userSignupDTO.getName());
		newUser.setEmail(userSignupDTO.getEmail());
		newUser.setPassword(passwordEncoder.encode(userSignupDTO.getPassword()));
		userRepository.save(newUser);
	}

}
