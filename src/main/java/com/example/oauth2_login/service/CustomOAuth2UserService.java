package com.example.oauth2_login.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.oauth2_login.model.Role;
import com.example.oauth2_login.model.User;
import com.example.oauth2_login.repository.RoleRepository;
import com.example.oauth2_login.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	//TODO: to simplify
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    	
        // Delegate to default implementation to get OAuth2 user
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = 
            new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        String providerId = userRequest.getClientRegistration().getRegistrationId();
        String providerUserId;
        String email;
        String name;
        String imageUrl;

        if ("google".equalsIgnoreCase(providerId)) {
            providerUserId = oauth2User.getAttribute("sub");
            email = oauth2User.getAttribute("email");
            name = oauth2User.getAttribute("name");
            imageUrl = oauth2User.getAttribute("picture");
        } else if ("github".equalsIgnoreCase(providerId)) {
            providerUserId = oauth2User.getAttribute("id").toString(); 
            email = oauth2User.getAttribute("email"); // May return null if email is private
            name = oauth2User.getAttribute("name");
            imageUrl = oauth2User.getAttribute("avatar_url");
        } else {
            throw new OAuth2AuthenticationException(
                "Unsupported provider: " + providerId);
        }

        // Find or create user
        User user = userRepository.findByProviderIdAndProviderUserId(providerId, providerUserId)
                .orElseGet(() -> {
                    User newUser = new User(providerId, providerUserId, email, name, imageUrl);
                    Role role = roleRepository.findByName(Role.RoleType.ROLE_USER)
                    		.orElseThrow(() -> new RuntimeException("Role USER not found"));
                    newUser.getRoles().add(role);
                    return userRepository.save(newUser);
                });
        
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        return new DefaultOAuth2User(
        	getAuthorities(user.getRoles()), 
            oauth2User.getAttributes(), 
            "email" // or the attribute you want to use as the name
        );

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
