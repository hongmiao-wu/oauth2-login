package com.example.oauth2_login.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.oauth2_login.model.CustomOAuth2User;
import com.example.oauth2_login.model.User;
import com.example.oauth2_login.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//    	log.info("OAuth2 Login attempt for provider: {}", 
//                userRequest.getClientRegistration().getRegistrationId());
    	
        // Delegate to default implementation to get OAuth2 user
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = 
            new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        // Extract key information
        String providerUserId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String profileImage = oauth2User.getAttribute("picture");

        // Find or create user
        User user = userRepository.findByProviderUserId(providerUserId)
            .orElseGet(() -> createNewUser(oauth2User));

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Return custom OAuth2 user
        return new CustomOAuth2User(user, oauth2User.getAttributes());
    }

    private User createNewUser(OAuth2User oauth2User) {
        User newUser = new User();
        newUser.setProviderUserId(oauth2User.getAttribute("sub"));
        newUser.setEmail(oauth2User.getAttribute("email"));
        newUser.setName(oauth2User.getAttribute("name"));
        newUser.setImageUrl(oauth2User.getAttribute("picture"));
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setLastLogin(LocalDateTime.now());
        return userRepository.save(newUser);
    }
}
