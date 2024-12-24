package com.hana4.keywordhanaro.model.mapper;

import java.util.stream.Collectors;

import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.dto.MultiKeywordDto;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;

public class KeywordMapper {
	public static KeywordDto toDto(Keyword keyword) {
		if (keyword == null) {
			return null;
		}
		KeywordDto dto = KeywordDto.builder()
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

		// 순환 참조 방지: 필요한 경우에만 MultiKeyword DTO를 매핑
		if (keyword.getMultiKeywords() != null && !keyword.getMultiKeywords().isEmpty()) {
			dto.setMultiKeyword(keyword.getMultiKeywords().stream()
				.map(mk -> MultiKeywordDto.builder()
					.id(mk.getId()) // 필요한 필드만 변환
					.keyword(SubKeywordMapper.toDto(mk.getKeyword()))
					.seqOrder(mk.getSeqOrder())
					.multiKeyword(SubKeywordMapper.toDto(mk.getMultiKeyword()))
					.build())
				.collect(Collectors.toList()));
		}

		return dto;
	}

	public static Keyword toEntity(KeywordDto keywordDto) {
		if (keywordDto == null) {
			return null;
		}

		return new Keyword(keywordDto.getId(), UserResponseMapper.toEntity(keywordDto.getUser()),
			keywordDto.getName(), KeywordType.valueOf(keywordDto.getType()),
			keywordDto.getDesc());
	}
}
