package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.UserAccountDto;
import com.hana4.keywordhanaro.model.entity.user.User;

public class UserAccountMapper {

	public static UserAccountDto toDto(User user) {
		if (user == null) {
			return null;
		}

		return UserAccountDto.builder()
			.id(user.getId())
			.name(user.getName()).build();
	}

}
