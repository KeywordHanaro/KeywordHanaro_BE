package com.hana4.keywordhanaro.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.KeywordDTO;
import com.hana4.keywordhanaro.model.dto.TicketDTO;
import com.hana4.keywordhanaro.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TicketController {
	private final TicketService ticketService;

	@PostMapping("/ticket")
	public ResponseEntity<TicketDTO> createTicket(@RequestParam KeywordDTO keywordDTO) throws IOException {
		return ResponseEntity.ok(ticketService.createTicket(keywordDTO.getId()));
	}
}
