package com.example.oauth2_login.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
	private Integer ID;
	private String email;
	private String name;
	private String imageUrl;
}
