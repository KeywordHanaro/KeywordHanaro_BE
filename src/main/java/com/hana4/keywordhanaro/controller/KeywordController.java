package com.hana4.keywordhanaro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.mapper.KeywordMapper;
import com.hana4.keywordhanaro.service.KeywordService;
import com.hana4.keywordhanaro.utils.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/keyword")
@RequiredArgsConstructor
public class KeywordController {

	private final KeywordService keywordService;
	private final UserDetailsService userDetailsService;

	@PostMapping
	public ResponseEntity<KeywordDto> createKeyword(@RequestBody KeywordDto keywordDto, Authentication authentication) {
		String userName = authentication.getName();
		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(userName);
		keywordDto.setUser(userDetails.getUser());
		return ResponseEntity.ok(keywordService.createKeyword(keywordDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<KeywordMapper.DeleteResponse> deleteKeyword(@PathVariable Long id) {
		return keywordService.removeKeyword(id);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<KeywordDto> updateKeyword(@PathVariable Long id, @RequestBody KeywordDto keywordDto,
		Authentication authentication) {
		String userName = authentication.getName();
		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(userName);
		keywordDto.setUser(userDetails.getUser());
		return ResponseEntity.ok(keywordService.updateKeyword(id, keywordDto));
	}
}
