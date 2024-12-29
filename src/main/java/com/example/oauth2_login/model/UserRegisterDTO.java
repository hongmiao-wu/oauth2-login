package com.example.oauth2_login.model;

import lombok.Data;

@Data
public class UserRegisterDTO {
	private String name;
	private String email;
    private String password;
}
