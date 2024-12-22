package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.model.dto.TicketDto;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;

public interface TicketService {
	TicketDto createTicket(TicketRequestDto ticketRequestDTO) throws Exception;
}
