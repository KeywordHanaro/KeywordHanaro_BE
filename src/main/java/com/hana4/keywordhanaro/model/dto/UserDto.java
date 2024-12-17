package com.hana4.keywordhanaro.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends BaseDto {
	private String id;
	private String username;
	private String password;
	private String name;
	private String status;
	private String accessToken;
	private String refreshToken;
	private String email;
	private String tel;
}
