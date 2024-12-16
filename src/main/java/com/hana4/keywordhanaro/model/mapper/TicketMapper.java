package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.TicketDTO;
import com.hana4.keywordhanaro.model.entity.Ticket;

public class TicketMapper {
	public static TicketDTO toDTO(Ticket ticket) {
		return TicketDTO.builder()
			.id(ticket.getId())
			.userId(ticket.getUser().getId())
			.branchId(ticket.getBranchId())
			.branchName(ticket.getBranchName())
			.waitingNumber(ticket.getWaitingNumber())
			.createAt(ticket.getCreateAt())
			.build();
	}
}
