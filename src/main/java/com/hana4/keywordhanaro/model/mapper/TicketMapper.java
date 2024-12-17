package com.hana4.keywordhanaro.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.TicketDto;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;
import com.hana4.keywordhanaro.model.entity.Ticket;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.user.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TicketMapper {
	private static final ObjectMapper mapper = new ObjectMapper();

	public static TicketDto toDTO(Ticket ticket) {
		return TicketDto.builder()
			.id(ticket.getId())
			.user(ticket.getUser())
			.branchId(ticket.getBranchId())
			.branchName(ticket.getBranchName())
			.workNumber(ticket.getWorkNumber())
			.waitingGuest(ticket.getWaitingGuest())
			.waitingNumber(ticket.getWaitingNumber())
			.createAt(ticket.getCreateAt())
			.build();
	}

	public static Ticket toTicket_1(Keyword keyword, TicketRequestDto requestDTO, Long waitingNumber,
		Long waitingGuest) throws
		JsonProcessingException {
		JsonNode branchInfo = mapper.readTree(keyword.getBranch());

		return new Ticket(branchInfo.get("id").asLong(), branchInfo.get("place_name").asText(), keyword.getUser(),
			waitingGuest, waitingNumber, requestDTO.getWorkNumber());
	}

	public static Ticket toTicket_2(User user, TicketRequestDto requestDTO, Long waitingNumber,
		Long waitingGuest) throws
		JsonProcessingException {

		return new Ticket(requestDTO.getBranchId(), requestDTO.getBranchName(), user,
			waitingGuest, waitingNumber, requestDTO.getWorkNumber());
	}
}
