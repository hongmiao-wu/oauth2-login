package com.example.oauth2_login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.oauth2_login.service.CustomOAuth2UserService;
import com.example.oauth2_login.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig 
{
	@Autowired
    private CustomOAuth2UserService customOAuth2UserService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception
	{
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsServiceImpl)
				.passwordEncoder(passwordEncoder())
				.and()
				.build();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
	{
		return http.
				csrf(csrf -> csrf.disable()). // disable for testing
				authorizeHttpRequests(auth -> {
					auth.requestMatchers("/", "user/signup", 
							"/swagger-ui.html",
						    "/swagger-ui/**", 
						    "/v3/api-docs*/**", 
						    "/swagger-resources/**").permitAll();
					auth.anyRequest().authenticated();
				})
				.oauth2Login(oauth2 -> oauth2
		                .userInfoEndpoint(userInfo -> 
	                    userInfo.userService(customOAuth2UserService)
	                ).defaultSuccessUrl("/secured", true)
	            )
				.formLogin(formLogin -> formLogin
						.defaultSuccessUrl("/secured", true)
						.permitAll())
				.build();
	}
}


