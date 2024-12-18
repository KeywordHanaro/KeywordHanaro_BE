package com.hana4.keywordhanaro.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.model.dto.TransactionDto;
import com.hana4.keywordhanaro.service.InquiryServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inquiry")
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
public class InquiryController {
	private final InquiryServiceImpl inquiryService;

	@Operation(
		summary = "거래 내역 조회",
		description = "조회기간, 거래구분, 정렬순서, 검색어로 필터링하여 조회합니다",
		parameters = {
			@Parameter(name = "accountId", description = "accountId", required = true, example = "9"),
			@Parameter(name = "startDate", description = "검색 시작일 YYYY-mm-dd", required = true, example = "2024-01-01"),
			@Parameter(name = "endDate", description = "검색 종료일 YYYY-mm-dd", required = true, example = "2024-12-31"),
			@Parameter(name = "transactionType", description = "all-전체조회/deposit-입금만조회/withdraw-출금만조회", example = "all"),
			@Parameter(name = "sortOrder", description = "latest-최신순/oldest 오래된순 정렬", example = "latest"),
			@Parameter(name = "searchWord", description = "검색어", example = "밥값")
		}
	)

	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공적으로 거래 내역을 조회하였습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (날짜 형식 오류, null 값, 날짜 범위 오류, 정렬 순서 오류, 거래 유형 오류 등)",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Error description\" }"
			))),
		@ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 404, \"error\": \"Not Found\", \"message\": \"Account not found with the given accountId.\" }"
			))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"서버에러 메시지.\" }"
			)))
	})
	@GetMapping("/{accountId}")
	public ResponseEntity<List<TransactionDto>> getAccountTransactions(
		@PathVariable Long accountId,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
		@RequestParam(defaultValue = "all") String transactionType,
		@RequestParam(defaultValue = "latest") String sortOrder,
		@RequestParam(required = false) String searchWord) {

		LocalDate parsedStartDate;
		LocalDate parsedEndDate;

		try {
			parsedStartDate = LocalDate.parse(startDate.trim());
			parsedEndDate = LocalDate.parse(endDate.trim());
		} catch (DateTimeParseException e) {
			throw new InvalidRequestException("Invalid date format. Please provide valid date format (yyyy-MM-dd).");
		}

		List<TransactionDto> transactions = inquiryService.getAccountTransactions(
			accountId, parsedStartDate, parsedEndDate, transactionType, sortOrder, searchWord);

		return ResponseEntity.ok(transactions);
	}
}
