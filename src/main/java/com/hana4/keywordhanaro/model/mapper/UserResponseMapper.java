package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.UserResponseDto;
import com.hana4.keywordhanaro.model.entity.user.User;

public class UserResponseMapper {

	public static UserResponseDto toDto(User user) {
		if (user == null) {
			return null;
		}

		return UserResponseDto.builder()
			.id(user.getId())
			.username(user.getUsername())
			.name(user.getName())
			.status(user.getStatus())
			.email(user.getEmail())
			.tel(user.getTel())
			.permission(user.getPermission())
			.build();
	}

	public static User toEntity(UserResponseDto userDto) {
		if (userDto == null) {
			return null;
		}

		return new User(userDto.getId(), userDto.getUsername(), userDto.getName(),
			userDto.getStatus(), userDto.getEmail(), userDto.getTel(), userDto.getPermission());
	}
}
