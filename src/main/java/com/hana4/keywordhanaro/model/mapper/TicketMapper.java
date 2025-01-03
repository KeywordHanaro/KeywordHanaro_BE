package com.hana4.keywordhanaro.model.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.TicketDto;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;
import com.hana4.keywordhanaro.model.entity.Ticket;
import com.hana4.keywordhanaro.model.entity.user.User;

public class TicketMapper {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static TicketDto toDto(Ticket ticket) {
		if (ticket == null) {
			return null;
		}

		return TicketDto.builder()
			.id(ticket.getId())
			// .user(UserMapper.toDto(ticket.getUser()))
			.branchId(ticket.getBranchId())
			.branchName(ticket.getBranchName())
			.workNumber(ticket.getWorkNumber())
			.waitingGuest(ticket.getWaitingGuest())
			.waitingNumber(ticket.getWaitingNumber())
			.createAt(ticket.getCreateAt())
			.build();
	}

	public static Ticket toEntity(TicketDto ticketDto) {
		if (ticketDto == null) {
			return null;
		}

		return new Ticket(ticketDto.getId(), null, ticketDto.getBranchId(),
			ticketDto.getBranchName(),
			ticketDto.getWaitingNumber(), ticketDto.getWaitingGuest(), ticketDto.getWorkNumber(),
			ticketDto.getCreateAt());
	}

	public static Ticket toEntity(User user, TicketRequestDto requestDTO, Long waitingNumber,
		Long waitingGuest) {
		if (requestDTO == null) {
			return null;
		}
		return new Ticket(requestDTO.getBranchId(), requestDTO.getBranchName(), user,
			waitingGuest, waitingNumber, requestDTO.getWorkNumber());
	}

}
