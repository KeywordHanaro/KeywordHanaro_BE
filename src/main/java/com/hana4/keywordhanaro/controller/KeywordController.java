package com.hana4.keywordhanaro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.DeleteResponseDto;
import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.dto.KeywordResponseDto;
import com.hana4.keywordhanaro.service.KeywordService;
import com.hana4.keywordhanaro.utils.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/keyword")
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
public class KeywordController {

	private final KeywordService keywordService;
	private final UserDetailsService userDetailsService;

	@Operation(summary = "키워드 생성", description = "사용자가 송금, 조회, 정산, 번호표 중 키워드를 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "키워드 생성 성공", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "사용자 또는 계좌를 찾을 수 없음", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
	})
	@PostMapping
	public ResponseEntity<KeywordDto> createKeyword(@RequestBody KeywordDto keywordDto, Authentication authentication) {
		String userName = authentication.getName();
		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(userName);
		keywordDto.setUser(userDetails.getUser());
		return ResponseEntity.ok(keywordService.createKeyword(keywordDto));
	}

	@Operation(summary = "키워드 삭제", description = "ID에 해당하는 키워드를 삭제합니다.",
		parameters = {
			@Parameter(name = "id", description = "keywordId", required = true, example = "1")})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "키워드 생성 성공", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "사용자 또는 계좌를 찾을 수 없음", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<DeleteResponseDto> deleteKeyword(@PathVariable Long id) {
		return keywordService.removeKeyword(id);
	}

	@Operation(summary = "키워드 수정", description = "ID에 해당하는 키워드를 수정합니다.",
		parameters = {
			@Parameter(name = "id", description = "keywordId", required = true, example = "2")})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "키워드 수정 성공", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "키워드, 사용자 또는 계좌를 찾을 수 없음", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
	})
	@PatchMapping("/{id}")
	public ResponseEntity<KeywordDto> updateKeyword(@PathVariable Long id, @RequestBody KeywordDto keywordDto,
		Authentication authentication) {
		String userName = authentication.getName();
		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(userName);
		keywordDto.setUser(userDetails.getUser());
		return ResponseEntity.ok(keywordService.updateKeyword(id, keywordDto));
	}

	@PostMapping("/use")
	public ResponseEntity<KeywordResponseDto> useKeyword(@RequestBody KeywordDto keywordDto) {
		KeywordResponseDto response = keywordService.useKeyword(keywordDto);
		return ResponseEntity.ok(response);
	}
}
