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

import com.hana4.keywordhanaro.model.dto.TransactionDTO;
import com.hana4.keywordhanaro.service.InquiryServiceImpl;

@RestController
@RequestMapping("/inquiry")
@ResponseStatus(HttpStatus.OK)
public class InquiryController {
	private final InquiryServiceImpl inquiryService;

	public InquiryController(InquiryServiceImpl inquiryService) {
		this.inquiryService = inquiryService;
	}

	@GetMapping("/{accountId}")
	public ResponseEntity<List<TransactionDTO>> getAccountTransactions(
		@PathVariable Long accountId,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
		@RequestParam(defaultValue = "all") String transactionType,
		@RequestParam(defaultValue = "latest") String sortOrder,
		@RequestParam(required = false) String searchWord) {

		LocalDate parsedStartDate = LocalDate.parse(startDate.trim());
		LocalDate parsedEndDate = LocalDate.parse(endDate.trim());

		List<TransactionDTO> transactions = inquiryService.getAccountTransactions(
			accountId, parsedStartDate, parsedEndDate, transactionType, sortOrder, searchWord);

		return ResponseEntity.ok(transactions);
	}
}
