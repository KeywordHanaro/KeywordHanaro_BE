package com.hana4.keywordhanaro.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecentTransactionDto {
	private Long accountId;
	private Long partnerAccountId;
	private String partnerAccountNumber;
	private LocalDateTime lastTransactionTime;
}
