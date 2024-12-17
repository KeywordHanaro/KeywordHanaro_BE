package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.TicketDto;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;
import com.hana4.keywordhanaro.model.entity.Ticket;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;

public class TicketMapper {
	public static TicketDto toDTO(Ticket ticket) {
		return TicketDto.builder()
			.id(ticket.getId())
			.user(ticket.getUser())
			.branchId(ticket.getBranchId())
			.branchName(ticket.getBranchName())
			.waitingNumber(ticket.getWaitingNumber())
			.createAt(ticket.getCreateAt())
			.build();
	}

	public static Ticket toTicket_1(Keyword keyword, TicketRequestDto requestDTO)
	{

		return new Ticket(keyword.getUser(), requestDTO.getBranchId(), requestDTO.getBranchName(), requestDTO.)
	}
}
