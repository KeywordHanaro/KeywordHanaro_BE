package com.hana4.keywordhanaro.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.mapper.UserResponseMapper;
import com.hana4.keywordhanaro.service.KeywordService;
import com.hana4.keywordhanaro.utils.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/keyword")
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
@Tag(name = "Keyword", description = "키워드 관련 API")
public class KeywordController {

	private final KeywordService keywordService;
	private final UserDetailsService userDetailsService;

	@Operation(summary = "키워드 생성", description = "사용자가 송금, 조회, 정산, 번호표 중 키워드를 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "키워드 생성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Error description\" }"
			))),
		@ApiResponse(responseCode = "404", description = "사용자 또는 계좌를 찾을 수 없음",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 404, \"error\": \"Not Found\", \"message\": \"User or Account not found\" }"
			))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"server error message\" }")))})
	@PostMapping
	public ResponseEntity<KeywordDto> createKeyword(@RequestBody KeywordDto keywordDto,
		Authentication authentication) throws Exception {
		String username = authentication.getName();
		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(username);
		keywordDto.setUser(UserResponseMapper.toDto(userDetails.getUser()));
		return ResponseEntity.ok(keywordService.createKeyword(keywordDto));
	}

	@Operation(summary = "키워드 삭제", description = "ID에 해당하는 키워드를 삭제합니다.",
		parameters = {
			@Parameter(name = "id", description = "keywordId", required = true, example = "1")})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "키워드 생성 성공",
			content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "사용자 또는 계좌를 찾을 수 없음",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 404, \"error\": \"Not Found\", \"message\": \"User or Account not found\" }"
			))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"server error message\" }")))
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<DeleteResponseDto> deleteKeyword(@PathVariable Long id) {
		return keywordService.removeKeyword(id);
	}

	@Operation(summary = "키워드 수정", description = "ID에 해당하는 키워드를 수정합니다.",
		parameters = {
			@Parameter(name = "id", description = "keywordId", required = true, example = "2")})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "키워드 수정 성공",
			content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Error description\" }"))),
		@ApiResponse(responseCode = "404", description = "키워드, 사용자 또는 계좌를 찾을 수 없음",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 404, \"error\": \"Not Found\", \"message\": \"User or Account not found\" }"
			))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"server error message\" }")))})
	@PatchMapping("/{id}")
	public ResponseEntity<KeywordDto> updateKeyword(@PathVariable Long id, @RequestBody KeywordDto keywordDto,
		Authentication authentication) {
		String username = authentication.getName();
		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(username);
		keywordDto.setUser(UserResponseMapper.toDto(userDetails.getUser()));
		return ResponseEntity.ok(keywordService.updateKeyword(id, keywordDto));
	}

	@Operation(summary = "키워드 사용", description = "해당 키워드를 실행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "키워드 생성 성공",
			content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "키워드를 찾을 수 없음",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 404, \"error\": \"Not Found\", \"message\": \"Keyword not found\" }"
			))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"server error message\" }")))})
	@GetMapping("/{id}")
	public ResponseEntity<KeywordResponseDto> useKeyword(@PathVariable Long id) throws Exception {
		return ResponseEntity.ok(keywordService.useKeyword(id));
	}

	@Operation(summary = "내 모든 키워드 조회", description = "내 모든 키워드를 조회합니다")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "키워드 생성 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = KeywordDto.class)))),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
	})
	@GetMapping
	public ResponseEntity<List<KeywordDto>> getKeywords(Authentication authentication) {
		String username = authentication.getName();
		return ResponseEntity.ok(keywordService.getKeywordsByUsername(username));
	}
}
