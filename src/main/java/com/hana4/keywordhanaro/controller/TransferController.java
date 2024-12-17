package com.hana4.keywordhanaro.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.TransactionDto;
import com.hana4.keywordhanaro.model.dto.TransferRequestDto;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.mapper.AccountMapper;
import com.hana4.keywordhanaro.service.TransferService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tranfer")
@RequiredArgsConstructor
public class TransferController {
	private final TransferService transferService;

	@PostMapping
	public ResponseEntity<TransactionDto> transfer(@RequestBody TransferRequestDto transferRequestDTO) {
		Transaction transaction = transferService.transfer(
			transferRequestDTO.getFromAccountNumber(),
			transferRequestDTO.getToAccountNumber(),
			transferRequestDTO.getAmount()
		);
		TransactionDto responseDTO = TransactionDto.builder()
			.account(AccountMapper.toDTO(transaction.getAccount()))
			.subAccount(AccountMapper.toDTO(transaction.getSubAccount()))
			.amount(transaction.getAmount())
			.status("SUCCESS")
			.beforeBalance(transaction.getAccount().getBalance())
			.build();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Transaction-Time", LocalDateTime.now().toString());

		return ResponseEntity
			.status(HttpStatus.OK)
			.headers(headers)
			.body(responseDTO);

	}
}
