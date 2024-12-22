package com.hana4.keywordhanaro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hana4.keywordhanaro.model.entity.user.UserStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserDto extends BaseDto {
	private String id;
	private String username;
	@JsonIgnore
	private String password;
	private String name;
	private UserStatus status;
	private String email;
	private String tel;
	private int permission;
	private TicketDto ticket;

	public UserDto(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
