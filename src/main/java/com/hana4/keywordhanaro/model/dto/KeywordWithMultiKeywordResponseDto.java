package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class KeywordWithMultiKeywordResponseDto extends KeywordResponseDto {
	private List<KeywordResponseDto> multiKeywords;

	public KeywordWithMultiKeywordResponseDto(Long id, UserResponseDto user, String type, String name,
		boolean isFavorite,
		String desc, Long seqOrder, AccountResponseDto account, AccountResponseDto subAccount, String inquiryWord,
		Boolean checkEveryTime, BigDecimal amount, List<GroupMemberDto> groupMember, BranchDto branch,
		List<TransactionDto> transactions) {
		super(id, user, type, name, isFavorite, desc, seqOrder, account, subAccount, inquiryWord, checkEveryTime,
			amount, groupMember, branch, null, transactions);
	}
}
