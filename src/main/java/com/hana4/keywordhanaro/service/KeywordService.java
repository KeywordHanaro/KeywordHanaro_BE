package com.hana4.keywordhanaro.service;

import org.springframework.http.ResponseEntity;

import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.mapper.KeywordMapper;

public interface KeywordService {
	public KeywordDto createKeyword(KeywordDto keywordDto);

	public KeywordDto updateKeyword(Long id, KeywordDto keywordDto);

	ResponseEntity<KeywordMapper.DeleteResponse> removeKeyword(Long id);
}
