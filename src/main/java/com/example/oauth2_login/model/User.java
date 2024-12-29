package com.example.oauth2_login.model;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	public User(String providerId, String providerUserId, String email, String name, String imageUrl)
	{
		this.providerId = providerId;
		this.providerUserId = providerUserId;
		this.email = email;
		this.name = name;
		this.imageUrl = imageUrl;
	}
	
	public User (String name, String email, String password, Set<Role> roles) 
	{
		this.name = name;
		this.email = email;
		this.password = password;
		this.roles = roles == null ? new HashSet<>() : new HashSet<>(roles);
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;
    
    private String password;

    private String providerId; // e.g., "google", "github"
    private String providerUserId;
    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    private LocalDateTime lastLogin;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

}
