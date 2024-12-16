package com.hana4.keywordhanaro.model.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketDTO {
	private Long id;
	private String userId;
	private Long branchId;
	private String branchName;
	private Long waitingNumber;
	private LocalDateTime createAt;
}
