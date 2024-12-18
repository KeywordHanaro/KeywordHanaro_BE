package com.hana4.keywordhanaro.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketRequestDto {
	private Long keywordId;
	private byte workNumber;
	private Long branchId;
	private String branchName;
	private String userId;
}
