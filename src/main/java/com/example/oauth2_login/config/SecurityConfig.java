package com.example.oauth2_login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.oauth2_login.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig 
{
	@Autowired
    private CustomOAuth2UserService customOAuth2UserService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
	{
		return http.
				authorizeHttpRequests(auth -> {
					auth.requestMatchers("/").permitAll();
					auth.anyRequest().authenticated();
				})
				.oauth2Login(oauth2 -> oauth2
		                .userInfoEndpoint(userInfo -> 
	                    userInfo.userService(customOAuth2UserService)
	                ).defaultSuccessUrl("/secured", true)
	            )
				.formLogin(Customizer.withDefaults())
				.build();
	}
}


