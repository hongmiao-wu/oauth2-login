package com.example.oauth2_login.model;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(unique = true)
	private RoleType name; 
	
	public enum RoleType
	{
		ROLE_USER,
		ROLE_STAFF,
		ROLE_ADMIN
	}
	
	public Role(RoleType name) {
        this.name = name;
    }
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Permission> permissions = new HashSet<>();

}
