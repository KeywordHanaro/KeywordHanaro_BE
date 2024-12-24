package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SettlementReqDto {
	private String code;
	private BigDecimal amount;
	private AccountDto account;
	private List<UserDto> groupMember;
	private String type;
}
