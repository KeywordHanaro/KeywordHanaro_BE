package com.hana4.keywordhanaro.controller;

import java.time.LocalDate;
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

import com.hana4.keywordhanaro.model.dto.TransactionDto;
import com.hana4.keywordhanaro.service.InquiryServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/inquiry")
@ResponseStatus(HttpStatus.OK)
public class InquiryController {
	private final InquiryServiceImpl inquiryService;

	public InquiryController(InquiryServiceImpl inquiryService) {
		this.inquiryService = inquiryService;
	}

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
	@GetMapping("/{accountId}")
	public ResponseEntity<List<TransactionDto>> getAccountTransactions(
		@PathVariable Long accountId,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
		@RequestParam(defaultValue = "all") String transactionType,
		@RequestParam(defaultValue = "latest") String sortOrder,
		@RequestParam(required = false) String searchWord) {

		LocalDate parsedStartDate = LocalDate.parse(startDate.trim());
		LocalDate parsedEndDate = LocalDate.parse(endDate.trim());

		List<TransactionDto> transactions = inquiryService.getAccountTransactions(
			accountId, parsedStartDate, parsedEndDate, transactionType, sortOrder, searchWord);

		return ResponseEntity.ok(transactions);
	}
}
