package com.hana4.keywordhanaro.model.mapper;

import java.util.List;

import com.hana4.keywordhanaro.model.dto.BranchDto;
import com.hana4.keywordhanaro.model.dto.GroupMemberDto;
import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.dto.KeywordResponseDto;
import com.hana4.keywordhanaro.model.dto.TransactionDto;

public class KeywordResponseMapper {
	public static KeywordResponseDto toDto(KeywordDto keywordDto, List<TransactionDto> transactions) {
		if (keywordDto == null) {
			return null;
		}

		return KeywordResponseDto.builder()
			.id(keywordDto.getId())
			.user(keywordDto.getUser())
			.type(keywordDto.getType())
			.name(keywordDto.getName())
			.isFavorite(keywordDto.isFavorite())
			.desc(keywordDto.getDesc())
			.seqOrder(keywordDto.getSeqOrder())
			.account(keywordDto.getAccount())
			.subAccount(keywordDto.getSubAccount())
			.inquiryWord(keywordDto.getInquiryWord())
			.checkEveryTime(keywordDto.getCheckEveryTime())
			.amount(keywordDto.getAmount())
			// .multiKeyword(keywordDto.getMultiKeyword())
			.transactions(transactions)
			.build();

	}

	public static KeywordResponseDto toDto(KeywordDto keywordDto, BranchDto branchJson,
		List<GroupMemberDto> groupMemberJson) {
		if (keywordDto == null) {
			return null;
		}

		return KeywordResponseDto.builder()
			.id(keywordDto.getId())
			.user(keywordDto.getUser())
			.type(keywordDto.getType())
			.name(keywordDto.getName())
			.isFavorite(keywordDto.isFavorite())
			.desc(keywordDto.getDesc())
			.seqOrder(keywordDto.getSeqOrder())
			.account(keywordDto.getAccount())
			.subAccount(keywordDto.getSubAccount())
			.inquiryWord(keywordDto.getInquiryWord())
			.checkEveryTime(keywordDto.getCheckEveryTime())
			.amount(keywordDto.getAmount())
			.groupMember(groupMemberJson)
			.branch(branchJson)
			// .multiKeyword(keywordDto.getMultiKeyword())
			.build();

	}
}
