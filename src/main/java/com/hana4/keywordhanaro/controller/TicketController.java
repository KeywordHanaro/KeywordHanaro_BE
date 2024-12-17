package com.hana4.keywordhanaro.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.TicketDto;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;
import com.hana4.keywordhanaro.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TicketController {
	private final TicketService ticketService;

	@PostMapping("/ticket")
	public ResponseEntity<TicketDto> createTicket(@RequestBody TicketRequestDto ticketRequestDTO) throws IOException {
		return ResponseEntity.ok(ticketService.createTicket(ticketRequestDTO));
	}
}
