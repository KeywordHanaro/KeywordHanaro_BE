package com.hana4.keywordhanaro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.model.dto.TransactionDto;
import com.hana4.keywordhanaro.model.dto.TransferRequestDto;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.mapper.AccountResponseMapper;
import com.hana4.keywordhanaro.service.TransferService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
@Tag(name = "Transfer", description = "송금 API")
public class TransferController {
	private final TransferService transferService;

	@Operation(summary = "송금 요청", description = "송금자 계좌번호, 수취자 계좌번호, 송금 금액을 받아 이체를 수행합니다.")
	@Parameters({
		@Parameter(
			name = "TransferRequestDto",
			description = "송금 요청 정보 (송금자, 수취자, 금액)",
			required = true,
			schema = @Schema(implementation = TransferRequestDto.class)
		)})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "송금 요청이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Error description\" }"
			))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"server error message\" }")))})
	@PostMapping
	public ResponseEntity<TransactionDto> transfer(
		@RequestBody @Parameter(description = "송금 요청 데이터", required = true) TransferRequestDto transferRequestDto)
		throws AccountNotFoundException {
		Transaction transaction = transferService.transfer(
			transferRequestDto.getFromAccountNumber(),
			transferRequestDto.getToAccountNumber(),
			transferRequestDto.getAmount()
		);
		TransactionDto responseDTO = TransactionDto.builder()
			.account(AccountResponseMapper.toDto(transaction.getAccount()))
			.subAccount(AccountResponseMapper.toDto(transaction.getSubAccount()))
			.amount(transaction.getAmount())
			.status("SUCCESS")
			.beforeBalance(transaction.getAccount().getBalance())
			.build();

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(responseDTO);
	}
}
