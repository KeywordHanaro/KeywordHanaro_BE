package com.hana4.keywordhanaro.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.hana4.keywordhanaro.model.dto.DeleteResponseDto;
import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.dto.KeywordResponseDto;

public interface KeywordService {
	KeywordDto createKeyword(KeywordDto keywordDto) throws Exception;

	KeywordDto updateKeyword(Long id, KeywordDto keywordDto);

	ResponseEntity<DeleteResponseDto> removeKeyword(Long id);

	KeywordResponseDto useKeyword(Long id) throws Exception;

	List<KeywordDto> getKeywordsByUsername(String username);

	List<KeywordDto> getFavoriteKeywordsByUsername(String username);
}
