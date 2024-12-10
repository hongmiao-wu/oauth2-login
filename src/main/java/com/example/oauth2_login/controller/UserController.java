package com.example.oauth2_login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2_login.model.UserInfoDTO;
import com.example.oauth2_login.model.UserSignupDTO;
import com.example.oauth2_login.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/all")
	public ResponseEntity<List<UserInfoDTO>> getAllUserInfoDTO()
	{
		List<UserInfoDTO> userInfoDTOs = userService.getAllUserInfoDTO();
		if (userInfoDTOs.isEmpty())
		{
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(userInfoDTOs);
	}
	
	
	@PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserSignupDTO userSignupDTO) {
        try {
            userService.registerUser(userSignupDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }
	
	@GetMapping("/me")
	public ResponseEntity<UserInfoDTO> getCurrentUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getName().equals("anonymousUser"))
		{
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		
		if (authentication.getPrincipal() instanceof OAuth2User)
		{
			OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
			OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
			String providerId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
			String providerUserId = switch (providerId.toLowerCase()) {
            	case "github" -> oauth2User.getAttribute("id").toString();
            	case "google" -> oauth2User.getAttribute("sub");
            	default -> null;
			};
			try {
	            UserInfoDTO userInfoDTO = userService.getUserInfoDTOByProviderIdAndProviderUserId(providerId, providerUserId);
	            return ResponseEntity.ok(userInfoDTO);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
		}
		
		UserInfoDTO userInfoDTO = userService.getUserInfoDTOByEmail(authentication.getName());
		return ResponseEntity.ok(userInfoDTO);
	}
	

}
