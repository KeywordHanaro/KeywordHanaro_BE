package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.MultiKeywordDto;
import com.hana4.keywordhanaro.model.entity.keyword.MultiKeyword;

public class MultiKeywordMapper {
	public static MultiKeywordDto toDto(MultiKeyword multiKeyword) {
		if (multiKeyword == null) {
			return null;
		}

		return MultiKeywordDto.builder().id(multiKeyword.getId())
			.multiKeyword(KeywordMapper.toDto(multiKeyword.getMultiKeyword()))
			.keyword(KeywordMapper.toDto(multiKeyword.getKeyword()))
			.seqOrder(multiKeyword.getSeqOrder()).build();
	}

	public static MultiKeyword toEntity(MultiKeywordDto multiKeywordDto) {
		if (multiKeywordDto == null) {
			return null;
		}

		return new MultiKeyword(multiKeywordDto.getId(), KeywordMapper.toEntity(multiKeywordDto.getMultiKeyword()),
			KeywordMapper.toEntity(multiKeywordDto.getKeyword()), multiKeywordDto.getSeqOrder());
	}
}
