package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeywordDto {
	private Long id;
	private UserResponseDto user;
	private String type;
	private String name;
	private boolean isFavorite = false;
	private String desc;
	private Long seqOrder;
	private AccountDto account;
	private AccountDto subAccount;
	private String inquiryWord;
	private Boolean checkEveryTime;
	private BigDecimal amount;
	private String groupMember;
	private String branch;

}
