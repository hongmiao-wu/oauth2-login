package com.example.oauth2_login.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.oauth2_login.config.JwtUtil;
import com.example.oauth2_login.model.LoginDTO;
import com.example.oauth2_login.model.Role;
import com.example.oauth2_login.model.User;
import com.example.oauth2_login.model.UserInfoDTO;
import com.example.oauth2_login.model.UserRegisterDTO;
import com.example.oauth2_login.repository.RoleRepository;
import com.example.oauth2_login.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;

	public void registerUser(UserRegisterDTO userSignupDTO) {

		if (userSignupDTO.getEmail() == null || userSignupDTO.getPassword() == null || userSignupDTO.getName() == null)
		{
			throw new IllegalArgumentException("Username, email and password are required.");
		}
		
		if (userRepository.findByEmail(userSignupDTO.getEmail()).isPresent())
		{
			throw new IllegalArgumentException("Email is already in use.");
		}
		
		User newUser = new User();
		Role role = roleRepository.findByName(Role.RoleType.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Role USER not found"));
		newUser.setName(userSignupDTO.getName());
		newUser.setEmail(userSignupDTO.getEmail());
		newUser.setPassword(passwordEncoder.encode(userSignupDTO.getPassword()));
		newUser.getRoles().add(role);
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
	
	
	public List<UserInfoDTO> getAllUserInfoDTO() {
        return userRepository.findAll().stream()
            .map(user -> new UserInfoDTO(
                user.getId(),  
                user.getEmail(),
                user.getName(),
                user.getImageUrl()
            ))
            .collect(Collectors.toList());
    }
	
	public String authenticate(LoginDTO loginDTO) 
	{
	      Authentication authentication= authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                		loginDTO.getEmail(),
	                		loginDTO.getPassword()
	                )
	        );
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	        String token = jwtUtil.generateToken(user.getName());
	        return token;
	}
	
	public User saveUser(User user)
	{
		return userRepository.save(user);
	}

}
