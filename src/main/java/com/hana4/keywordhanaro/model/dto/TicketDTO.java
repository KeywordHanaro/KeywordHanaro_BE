package com.hana4.keywordhanaro.model.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TicketDTO {
	private Long id;
	private String userId;
	private Long branchId;
	private Long waitingNumber;
	private LocalDateTime createAt;
}
