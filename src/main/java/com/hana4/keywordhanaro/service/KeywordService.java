package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.model.dto.KeywordDto;

public interface KeywordService {
	public KeywordDto createKeyword(KeywordDto keywordDto);

	public KeywordDto updateKeyword(Long id, KeywordDto keywordDto);
}
