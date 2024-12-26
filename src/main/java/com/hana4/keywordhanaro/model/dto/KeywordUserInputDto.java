package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeywordUserInputDto {
	protected UserResponseDto user;
	protected String type;
	protected String name;
	protected boolean isFavorite = false;
	protected String desc;
	protected Long seqOrder;
	protected AccountResponseDto account;
	protected AccountResponseDto subAccount;
	protected String inquiryWord;
	protected Boolean checkEveryTime;
	protected BigDecimal amount;
	protected String groupMember;
	protected String branch;
	protected List<Long> multiKeywordIds;
}
