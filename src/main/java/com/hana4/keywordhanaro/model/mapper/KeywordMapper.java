package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;

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
			.account(AccountResponseMapper.toDto(keyword.getAccount()))
			.subAccount(AccountResponseMapper.toDto(keyword.getSubAccount()))
			.isFavorite(keyword.isFavorite())
			.build();
	}

	public static Keyword toEntity(KeywordDto keywordDto) {
		if (keywordDto == null) {
			return null;
		}

		return new Keyword(keywordDto.getUser(), KeywordType.valueOf(keywordDto.getType()), keywordDto.getName(),
			keywordDto.getDesc(),
			keywordDto.getSeqOrder());
	}
}
