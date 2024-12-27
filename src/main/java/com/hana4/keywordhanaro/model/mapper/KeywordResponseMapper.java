package com.hana4.keywordhanaro.model.mapper;

import java.util.List;

import com.hana4.keywordhanaro.model.dto.BranchDto;
import com.hana4.keywordhanaro.model.dto.GroupMemberDto;
import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.dto.KeywordResponseDto;
import com.hana4.keywordhanaro.model.dto.MultiKeywordResponseDto;
import com.hana4.keywordhanaro.model.dto.TransactionDto;

public class KeywordResponseMapper {
	private static KeywordResponseDto.KeywordResponseDtoBuilder builder(KeywordDto keywordDto) {
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
			.amount(keywordDto.getAmount());
	}

	public static KeywordResponseDto toDto(KeywordDto keywordDto, List<TransactionDto> transactions) {
		return KeywordResponseMapper.builder(keywordDto)
			.transactions(transactions)
			.build();
	}

	public static KeywordResponseDto toDto(KeywordDto keywordDto, BranchDto branchJson,
		List<GroupMemberDto> groupMemberJson) {
		return KeywordResponseMapper.builder(keywordDto)
			.groupMember(groupMemberJson)
			.branch(branchJson)
			.build();
	}

	public static KeywordResponseDto toMultiDto(KeywordDto keywordDto, List<MultiKeywordResponseDto> multiKeywordResponses) {
		return KeywordResponseMapper.builder(keywordDto)
			.multiKeyword(multiKeywordResponses)
			.build();
	}
}
