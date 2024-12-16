package com.hana4.keywordhanaro.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.model.dto.TicketDTO;

@Service
public interface TicketService {
	public TicketDTO createTicket(String userId, Long keywordId) throws IOException;
}
