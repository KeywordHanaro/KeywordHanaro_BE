package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.MultiKeywordDto;
import com.hana4.keywordhanaro.model.entity.keyword.MultiKeyword;

public class MultiKeywordMapper {
	public static MultiKeywordDto toDto(MultiKeyword multiKeyword) {

		return MultiKeywordDto.builder()
			.id(multiKeyword.getId())
			.parentId(multiKeyword.getMultiKeyword().getId())
			.keyword(KeywordMapper.toDto(multiKeyword.getKeyword()))
			.seqOrder(multiKeyword.getSeqOrder())
			.build();
	}

}
