package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.model.entity.user.User;

public class UserMapper {
	public static UserDto toDto(User user) {
		if (user == null) {
			return null;
		}

		return UserDto.builder()
			.id(user.getId())
			.username(user.getUsername())
			.password(user.getPassword())
			.name(user.getName())
			.status(user.getStatus())
			.email(user.getEmail())
			.tel(user.getTel())
			.permission(user.getPermission())
			.ticket(TicketMapper.toDto(user.getTicket()))
			.createAt(user.getCreateAt())
			.updateAt(user.getUpdateAt())
			.build();
	}

	public static User toEntity(UserDto userDto) {
		if (userDto == null) {
			return null;
		}

		return new User(userDto.getId(), userDto.getUsername(), userDto.getPassword(), userDto.getName(),
			userDto.getStatus(), userDto.getEmail(), userDto.getTel(), userDto.getPermission(),
			TicketMapper.toEntity(userDto.getTicket()), userDto.getCreateAt(), userDto.getUpdateAt());
	}
}
