package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;

public class KeywordMapper {
	public static KeywordDto toDto(Keyword keyword) {
		if (keyword == null) {
			return null;
		}
		return KeywordDto.builder()
			.id(keyword.getId())
			.user(UserResponseMapper.toDto(keyword.getUser()))
			.type(keyword.getType().name())
			.name(keyword.getName())
			.desc(keyword.getDescription())
			.seqOrder(keyword.getSeqOrder())
			.inquiryWord(keyword.getInquiryWord())
			.checkEveryTime(keyword.getCheckEveryTime())
			.amount(keyword.getAmount())
			.groupMember(keyword.getGroupMember())
			.branch(keyword.getBranch())
			.account(AccountMapper.toDto(keyword.getAccount()))
			.subAccount(AccountMapper.toDto(keyword.getSubAccount()))
			.isFavorite(keyword.isFavorite())
			.build();
	}
}
