package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO extends BaseDTO {
	private String id;
	private String userId;
	private Short bankId;
	private String name;
	private String password;
	private BigDecimal balance;
	private BigDecimal transferLimit;
	private String type;
	private Boolean mine;
	private String status;
}
