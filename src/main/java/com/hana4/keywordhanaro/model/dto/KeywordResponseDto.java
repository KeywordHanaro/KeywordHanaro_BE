package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeywordResponseDto {
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
	private List<GroupMemberDto> groupMember;
	private BranchDto branch;
	private List<MultiKeywordDto> multiKeyword;
	private List<TransactionDto> transactions;

}
