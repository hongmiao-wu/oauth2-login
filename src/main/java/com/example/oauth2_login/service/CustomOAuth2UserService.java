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
                    return userRepository.save(newUser);
                });
        
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Return custom OAuth2 user
        return new CustomOAuth2User(user, oauth2User.getAttributes());
    }

    
}
