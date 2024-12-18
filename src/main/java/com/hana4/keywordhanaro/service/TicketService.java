package com.hana4.keywordhanaro.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.model.dto.TicketDto;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;

@Service
public interface TicketService {
	public TicketDto createTicket(TicketRequestDto ticketRequestDTO) throws IOException;
}