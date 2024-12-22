package com.hana4.keywordhanaro.model.dto;

import java.time.LocalDateTime;

import com.hana4.keywordhanaro.model.entity.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketDto {
	private Long id;
	private UserDto user;
	private Long branchId;
	private String branchName;
	private Long waitingNumber;
	private Long waitingGuest;
	private byte workNumber;
	private LocalDateTime createAt;
}
