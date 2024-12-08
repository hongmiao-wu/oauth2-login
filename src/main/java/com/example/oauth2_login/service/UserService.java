package com.example.oauth2_login.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oauth2_login.model.User;
import com.example.oauth2_login.model.UserInfoDTO;
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
			throw new IllegalArgumentException("Username, email and password are required.");
		}
		
		if (userRepository.findByEmail(userSignupDTO.getEmail()).isPresent())
		{
			throw new IllegalArgumentException("Email is already in use.");
		}
		
		User newUser = new User();
		newUser.setName(userSignupDTO.getName());
		newUser.setEmail(userSignupDTO.getEmail());
		newUser.setPassword(passwordEncoder.encode(userSignupDTO.getPassword()));
		newUser.setCreatedAt(LocalDateTime.now());
		userRepository.save(newUser);
	}
	
	public UserInfoDTO getUserInfoDTOByProviderIdAndProviderUserId(String providerId, String providerUserId)
	{
		User user = userRepository.findByProviderIdAndProviderUserId(providerId, providerUserId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));
		
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setID(user.getId());
		userInfoDTO.setName(user.getName());
		userInfoDTO.setEmail(user.getEmail());
		return userInfoDTO;
	}
	
	public UserInfoDTO getUserInfoDTOByEmail(String email)
	{
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found."));
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setID(user.getId());
		userInfoDTO.setName(user.getName());
		userInfoDTO.setEmail(user.getEmail());
		return userInfoDTO;
	}
	

}
