package com.hana4.keywordhanaro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		@ApiResponse(responseCode = "200", description = "성공적으로 계좌 정보를 조회함"),
		@ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 404, \"error\": \"Bad Request\", \"message\": \"Account not Found\" }"
			))),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 403, \"error\": \"Bad Request\", \"message\": \"Access denied\" }"
			)))
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> getAccount(
		@Parameter(description = "조회할 계좌의 ID") @PathVariable("id") Long id) throws AccountNotFoundException {
		return ResponseEntity.ok(accountService.getAccount(id));
	}

	@Operation(summary = "모든 계좌 조회", description = "관리자가 모든 계좌 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공적으로 모든 계좌 정보를 조회함"),
		@ApiResponse(responseCode = "403", description = "접근 권한 없음",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 403, \"error\": \"Bad Request\", \"message\": \"Access denied\" }"
			)))
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

	@Operation(summary = "계좌 비밀번호 확인", description = "계좌번호와 비밀번호를 입력받아 일치 여부를 확인합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "비밀번호 확인 성공",
			content = @Content(schema = @Schema(implementation = Boolean.class))),
		@ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음")
	})
	@PostMapping("/checkPassword")
	public ResponseEntity<Boolean> checkPassword(
		@Parameter(description = "확인할 계좌번호, 비밀번호") @RequestBody AccountDto accountDto) throws AccountNotFoundException {
		return ResponseEntity.ok(accountService.checkPassword(accountDto.getAccountNumber(), accountDto.getPassword()));
	}

	@Operation(summary = "예금주 확인", description = "계좌번호와 은행명을 입력받아 예금주 이름을 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "예금주 확인 성공"),
		@ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 404, \"error\": \"Bad Request\", \"message\": \"Account not Found\" }"
			)))
	})
	@PostMapping("/checkDepositor")
	public ResponseEntity<String> checkAccountNumberAndBank(
		@Parameter(description = "확인할 계좌번호, 은행명") @RequestBody AccountDto accountDto) throws AccountNotFoundException {
		return ResponseEntity.ok(
			accountService.checkAccountNumberAndBank(accountDto.getAccountNumber(), accountDto.getBank()));
	}
}
