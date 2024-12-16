package com.hana4.keywordhanaro.model.dto;

import java.time.LocalDateTime;

import com.hana4.keywordhanaro.model.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketDTO {
	private Long id;
	private User user;
	private Long branchId;
	private String branchName;
	private Long waitingNumber;
	private LocalDateTime createAt;
}
