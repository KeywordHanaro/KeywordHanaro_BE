package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDTO {
	private Long id;
	private String fromAccountId;
	private String toAccountId;
	private BigDecimal amount;
	private String type;
	private String alias;
	private BigDecimal beforeBalance;
	private BigDecimal afterBalance;
	private Timestamp createdAt;
	private String status;

}
