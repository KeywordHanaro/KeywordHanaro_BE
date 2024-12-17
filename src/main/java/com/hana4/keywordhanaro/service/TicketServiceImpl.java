package com.hana4.keywordhanaro.service;

import java.io.IOException;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.TicketDto;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;
import com.hana4.keywordhanaro.model.entity.Ticket;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.mapper.TicketMapper;
import com.hana4.keywordhanaro.repository.KeywordRepository;
import com.hana4.keywordhanaro.repository.TicketRepository;
import com.hana4.keywordhanaro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
	private final TicketRepository ticketRepository;
	private final KeywordRepository keywordRepository;
	private final UserRepository userRepository;
	private final ObjectMapper mapper;
	private final Random random = new Random();

	@Override
	public TicketDto createTicket(TicketRequestDto requestDTO) throws IOException {
		Long waitingNumber = (long)(random.nextInt(300) + 1); // 1 ~ 300
		Long waitingGuest = (long)(random.nextInt(10) + 1); // 1 ~ 10

		// 키워드 사용 시
		if (requestDTO.getKeywordId() != null) {
			Keyword findKeyword = keywordRepository.findById(requestDTO.getKeywordId()).orElseThrow();
			JsonNode branchInfo = mapper.readTree(findKeyword.getBranch());

			Ticket ticket = Ticket.builder()
				.user(findKeyword.getUser())
				.branchId(branchInfo.get("id").asLong())
				.branchName(branchInfo.get("place_name").asText())
				.workNumber(requestDTO.getWorkNumber())
				.waitingNumber(waitingNumber)
				.waitingGuest(waitingGuest)
				.build();
			ticket = ticketRepository.save(ticket);
			return TicketMapper.toDTO(ticket);
		} // 일반 사용 시
		else {
			User findUser = userRepository.findById(requestDTO.getUserId()).orElseThrow();
			Ticket ticket = Ticket.builder()
				.user(findUser)
				.branchId(requestDTO.getBranchId())
				.branchName(requestDTO.getBranchName())
				.workNumber(requestDTO.getWorkNumber())
				.waitingNumber(waitingNumber)
				.waitingGuest(waitingGuest)
				.build();
			ticket = ticketRepository.save(ticket);
			return TicketMapper.toDTO(ticket);
		}

		// // 이미 발급된 티켓 있으면 해당 티켓 반환
		// Optional<Ticket> findTicket = ticketRepository.findByUser(requestDTO.getUser());
		// if (findTicket.isPresent()) {
		// 	return TicketMapper.toDTO(findTicket.get());
		// }

	}
}
