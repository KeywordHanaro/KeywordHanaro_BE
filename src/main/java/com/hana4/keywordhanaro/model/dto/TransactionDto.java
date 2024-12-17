package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hana4.keywordhanaro.model.entity.transaction.TransactionType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDto {
	private Long id;
	private AccountDto account;
	private AccountDto subAccount;
	private BigDecimal amount;
	private TransactionType type;
	private String alias;
	private BigDecimal beforeBalance;
	private BigDecimal afterBalance;
	private LocalDateTime createAt;
	private String status;
}
