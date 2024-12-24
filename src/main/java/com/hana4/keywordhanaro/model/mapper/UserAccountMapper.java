package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.AccountUserDto;
import com.hana4.keywordhanaro.model.entity.user.User;

public class UserAccountMapper {

	public static AccountUserDto toDto(User user) {
		if (user == null) {
			return null;
		}

		return AccountUserDto.builder()
			.id(user.getId())
			.name(user.getName()).build();
	}

}
