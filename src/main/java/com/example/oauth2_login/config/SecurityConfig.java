package com.example.oauth2_login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.oauth2_login.service.CustomOAuth2UserService;
import com.example.oauth2_login.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig 
{
	@Autowired
    private CustomOAuth2UserService customOAuth2UserService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	
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
		return http
		        // Disable CSRF for stateless authentication
		        .csrf(csrf -> csrf.disable())
		        
		        // Authorization rules
		        .authorizeHttpRequests(auth -> {
		            auth.requestMatchers(
		                    "/", 
		                    "/auth/**", 
		                    "/swagger-ui.html", 
		                    "/swagger-ui/**", 
		                    "/v3/api-docs*/**", 
		                    "/swagger-resources/**"
		            ).permitAll(); // Publicly accessible endpoints
		            auth.requestMatchers("/user").hasRole("USER"); // Protect /user with ROLE_USER
		            auth.anyRequest().authenticated(); // All other endpoints require authentication
		        })
		        
		        // OAuth2 login configuration
		        .oauth2Login(oauth2 -> oauth2
		            .userInfoEndpoint(userInfo -> 
		                userInfo.userService(customOAuth2UserService)
		            )
		            .defaultSuccessUrl("/user", true)
		        )
		        
		        // Form login configuration (Optional, only include if you still need form login)
		        .formLogin(formLogin -> formLogin
		            .defaultSuccessUrl("/user", true)
		            .permitAll()
		        )
		        
		        // Add custom JWT filter
		        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
		        
		        // Build the security filter chain
		        .build();

	}
}


