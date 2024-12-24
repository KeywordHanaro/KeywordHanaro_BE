package com.hana4.keywordhanaro.model.dto;

import com.hana4.keywordhanaro.model.entity.user.UserStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {
	private String id;
	private String name;
	private UserStatus status;
	private int permission;
}
