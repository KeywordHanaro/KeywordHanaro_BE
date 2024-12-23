package com.hana4.keywordhanaro.service;

import org.springframework.http.ResponseEntity;

import com.hana4.keywordhanaro.model.dto.DeleteResponseDto;
import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.dto.KeywordResponseDto;

public interface KeywordService {
	KeywordDto createKeyword(KeywordDto keywordDto);

	KeywordDto updateKeyword(Long id, KeywordDto keywordDto);

	ResponseEntity<DeleteResponseDto> removeKeyword(Long id);

	KeywordResponseDto useKeyword(Long id) throws Exception;
}
