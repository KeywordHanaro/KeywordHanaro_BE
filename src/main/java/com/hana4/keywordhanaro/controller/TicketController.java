package com.hana4.keywordhanaro.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.TicketDto;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;
import com.hana4.keywordhanaro.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket")
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
public class TicketController {
	private final TicketService ticketService;

	@Operation(
		summary = "번호표 생성",
		description = "지점별 번호표를 생성합니다"
	)
	@PostMapping()
	public ResponseEntity<TicketDto> createTicket(@RequestBody TicketRequestDto ticketRequestDTO) throws IOException {
		return ResponseEntity.ok(ticketService.createTicket(ticketRequestDTO));
	}
}
