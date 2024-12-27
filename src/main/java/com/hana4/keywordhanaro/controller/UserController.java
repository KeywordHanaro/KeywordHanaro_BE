package com.hana4.keywordhanaro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.exception.UserNotFoundException;
import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {
	private final UserService userService;

	@Operation(summary = "마스터 비밀번호 확인", description = "사용자의 마스터 비밀번호를 확인합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "확인 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = Boolean.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "401", description = "인증 실패"),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
	})
	@GetMapping("/auth-master")
	public ResponseEntity<Boolean> checkMasterPassword(@RequestBody UserDto userDto, Authentication authentication) throws
		UserNotFoundException {
		return ResponseEntity.ok(userService.checkMasterPassword(authentication.getName(), userDto.getMasterPassword()));
	}
}
