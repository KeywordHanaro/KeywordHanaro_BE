package com.hana4.keywordhanaro.model.dto;

import com.hana4.keywordhanaro.model.entity.user.UserStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserResponseDto {
	private String id;
	private String username;
	private String name;
	private UserStatus status;
	private String email;
	private String tel;
	private int permission;
}
