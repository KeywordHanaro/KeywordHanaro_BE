package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.DocumentDto;
import com.hana4.keywordhanaro.model.entity.document.Document;

public class DocumentMapper {
	public static Document toEntity(DocumentDto documentDto) {
		if (documentDto == null) {
			return null;
		}
		return new Document(documentDto.getType(), AccountMapper.toEntity(documentDto.getAccount()),
			AccountMapper.toEntity(documentDto.getSubAccount()), documentDto.getAmount());
	}
}
