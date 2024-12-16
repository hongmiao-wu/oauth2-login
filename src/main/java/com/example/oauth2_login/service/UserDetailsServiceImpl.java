package com.example.oauth2_login.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.oauth2_login.model.Role;
import com.example.oauth2_login.model.User;
import com.example.oauth2_login.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
		UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(user.getEmail());
		userBuilder.password(user.getPassword());
		
//		List<GrantedAuthority> authorities = user.getRoles().stream()
//	            .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
//	            .collect(Collectors.toList());
//		userBuilder.authorities(authorities);
		userBuilder.authorities(getAuthorities(user.getRoles()));
		return userBuilder.build();
	}
	
	private List<GrantedAuthority> getAuthorities(Set<Role> roles) {
	    
	    List<GrantedAuthority> roleAuthorities = roles.stream()
	            .map(role -> new SimpleGrantedAuthority(role.getName().toString())) // e.g., "ROLE_ADMIN"
	            .collect(Collectors.toList());

	    List<GrantedAuthority> permissionAuthorities = roles.stream()
	            .flatMap(role -> role.getPermissions().stream()) // Flatten permissions across all roles
	            .map(permission -> new SimpleGrantedAuthority(permission.getName())) // e.g., "PRODUCT_CREATE"
	            .collect(Collectors.toList());

	    List<GrantedAuthority> authorities = new ArrayList<>();
	    authorities.addAll(roleAuthorities);
	    authorities.addAll(permissionAuthorities);

	    return authorities;
	}
	
}
