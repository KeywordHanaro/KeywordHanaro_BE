package com.hana4.keywordhanaro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Tag(name = "Account", description = "계좌 관련 API")
public class AccountController {
	private final AccountService accountService;

	@Operation(summary = "특정 계좌 조회", description = "관리자가 특정 ID의 계좌 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공적으로 계좌 정보를 조회함",
			content = @Content(schema = @Schema(implementation = AccountDto.class))),
		@ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음"),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> getAccount(
		@Parameter(description = "조회할 계좌의 ID") @PathVariable("id") Long id) throws AccountNotFoundException {
		return ResponseEntity.ok(accountService.getAccount(id));
	}

	@Operation(summary = "모든 계좌 조회", description = "관리자가 모든 계좌 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공적으로 모든 계좌 정보를 조회함",
			content = @Content(schema = @Schema(implementation = AccountDto.class))),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음")
	})
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<AccountDto>> getAccounts() {
		return ResponseEntity.ok(accountService.getAccounts());
	}

	@Operation(summary = "내 계좌 조회", description = "사용자가 자신의 모든 계좌 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공적으로 사용자의 계좌 정보를 조회함",
			content = @Content(schema = @Schema(implementation = AccountDto.class))),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음")
	})
	@GetMapping("/myaccounts")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<AccountDto>> getMyAccounts(Authentication authentication) {
		String username = authentication.getName();
		return ResponseEntity.ok(accountService.getAccountsByUsername(username));
	}
}
