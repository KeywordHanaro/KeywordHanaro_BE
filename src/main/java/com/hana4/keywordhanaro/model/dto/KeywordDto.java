package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeywordDto {
	private Long id;
	private UserResponseDto user;
	private String type;
	private String name;
	private boolean isFavorite = false;
	private String desc;
	private Long seqOrder;
	private AccountResponseDto account;
	private AccountResponseDto subAccount;
	private String inquiryWord;
	private Boolean checkEveryTime;
	private BigDecimal amount;
	private String groupMember;
	private String branch;
	private List<MultiKeywordDto> multiKeyword;
}
