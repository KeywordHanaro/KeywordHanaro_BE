package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.SubKeywordDto;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;

public class SubKeywordMapper {
	public static SubKeywordDto toDto(Keyword entity) {
		if (entity == null) {
			return null;
		}
		return SubKeywordDto.builder()
			.id(entity.getId())
			.name(entity.getName())
			.build();
	}

	public static Keyword toEntity(SubKeywordDto dto) {
		if (dto == null) {
			return null;
		}
		return Keyword.builder()
			.id(dto.getId())
			.name(dto.getName())
			.build();
	}
}
