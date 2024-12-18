package com.hana4.keywordhanaro.service;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.exception.KeywordNotFoundException;
import com.hana4.keywordhanaro.exception.UserNotFoundException;
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
	private final Random random = new Random();

	@Override
	public TicketDto createTicket(TicketRequestDto requestDto) throws IOException {
		Long waitingNumber = (long)(random.nextInt(300) + 1); // 1 ~ 300
		Long waitingGuest = (long)(random.nextInt(10) + 1); // 1 ~ 10

		Ticket ticket;

		// 키워드 사용 시
		if (requestDto.getKeywordId() != null) {
			Keyword findKeyword = keywordRepository.findById(requestDto.getKeywordId())
				.orElseThrow(KeywordNotFoundException::new);

			Optional<Ticket> checkTicket = ticketRepository.findByUser(findKeyword.getUser());
			if (checkTicket.isPresent()) {
				return TicketMapper.toDTO(checkTicket.get());
			}

			ticket = TicketMapper.toTicket_1(findKeyword, requestDto, waitingNumber, waitingGuest);
		} else { // 일반 사용 시
			User findUser = userRepository.findById(requestDto.getUserId())
				.orElseThrow(UserNotFoundException::new);

			Optional<Ticket> checkTicket = ticketRepository.findByUser(findUser);
			if (checkTicket.isPresent()) {
				return TicketMapper.toDTO(checkTicket.get());
			}

			ticket = TicketMapper.toTicket_2(findUser, requestDto, waitingNumber, waitingGuest);
		}

		ticket = ticketRepository.save(ticket);
		return TicketMapper.toDTO(ticket);

	}

}
