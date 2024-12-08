package com.example.oauth2_login.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {
	private Integer ID;
	private String email;
	private String name;
	private String imageUrl;
}
