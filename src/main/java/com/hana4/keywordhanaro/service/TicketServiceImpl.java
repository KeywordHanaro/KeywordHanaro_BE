package com.hana4.keywordhanaro.service;

import java.io.IOException;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.TicketDTO;
import com.hana4.keywordhanaro.model.entity.Ticket;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.mapper.TicketMapper;
import com.hana4.keywordhanaro.repository.KeywordRepository;
import com.hana4.keywordhanaro.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {
	private final TicketRepository ticketRepository;
	private final KeywordRepository keywordRepository;
	private final ObjectMapper mapper;
	private final Random random = new Random();

	public TicketServiceImpl(TicketRepository ticketRepository, KeywordRepository keywordRepository,
		ObjectMapper mapper) {
		this.ticketRepository = ticketRepository;
		this.keywordRepository = keywordRepository;
		this.mapper = mapper;
	}

	@Override
	public TicketDTO createTicket(String userId, Long keywordId) throws IOException {
		// keyword에서 브랜치 정보 조회
		Keyword keyword = keywordRepository.findById(keywordId).orElseThrow();

		JsonNode branchInfo = mapper.readTree(keyword.getBranch());

		Long waitingNumber = (long)(random.nextInt(300) + 1); // 1 ~ 300

		Ticket ticket = Ticket.builder()
			.user(keyword.getUser())
			.branchId(branchInfo.get("id").asLong())
			.branchName(branchInfo.get("place_name").asText())
			.waitingNumber(waitingNumber)
			.build();
		ticket = ticketRepository.save(ticket);

		return TicketMapper.toDTO(ticket);
	}
}
