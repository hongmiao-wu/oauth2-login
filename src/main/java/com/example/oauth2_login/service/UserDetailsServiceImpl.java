package com.example.oauth2_login.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.oauth2_login.model.User;
import com.example.oauth2_login.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
		UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(user.getEmail());
		userBuilder.password(user.getPassword());
		
		List<GrantedAuthority> authorities = user.getRoles().stream()
	            .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
	            .collect(Collectors.toList());
		userBuilder.authorities(authorities);
		return userBuilder.build();
	}
}
