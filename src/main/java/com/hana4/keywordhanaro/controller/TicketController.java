package com.hana4.keywordhanaro.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.TicketDTO;
import com.hana4.keywordhanaro.service.TicketService;

@RestController
public class TicketController {
	private final TicketService ticketService;

	public TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@PostMapping("/ticket")
	public TicketDTO createTicket(@RequestParam String userId, @RequestParam Long keywordId) throws IOException {
		return ticketService.createTicket(userId, keywordId);
	}
}
