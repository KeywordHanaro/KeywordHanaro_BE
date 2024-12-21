package com.hana4.keywordhanaro.service;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.exception.InvalidRequestException;
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
			validateWorkNumber(requestDto);

			Keyword findKeyword = keywordRepository.findById(requestDto.getKeywordId())
				.orElseThrow(() -> new NullPointerException("Keyword not found"));

			Optional<Ticket> checkTicket = ticketRepository.findByUser(findKeyword.getUser());
			if (checkTicket.isPresent()) {
				return TicketMapper.toDto(checkTicket.get());
			}

			ticket = TicketMapper.toEntity(findKeyword, requestDto, waitingNumber, waitingGuest);
		} else { // 일반 사용 시
			validateWorkNumber(requestDto);
			validateBranchIdAndName(requestDto);
			validateUser(requestDto);

			User findUser = userRepository.findById(requestDto.getUserId())
				.orElseThrow(() -> new NullPointerException("User not found"));

			Optional<Ticket> checkTicket = ticketRepository.findByUser(findUser);
			if (checkTicket.isPresent()) {
				return TicketMapper.toDto(checkTicket.get());
			}

			ticket = TicketMapper.toEntity(findUser, requestDto, waitingNumber, waitingGuest);
		}

		ticket = ticketRepository.save(ticket);
		return TicketMapper.toDto(ticket);

	}

	private void validateWorkNumber(TicketRequestDto requestDto) {
		if (requestDto.getWorkNumber() == 0) {
			throw new InvalidRequestException("Work number is required for keyword ticket");
		}
		if (requestDto.getWorkNumber() < 1 || requestDto.getWorkNumber() > 3) {
			throw new InvalidRequestException("Work number must be between 1 and 3");
		}
	}

	private void validateBranchIdAndName(TicketRequestDto requestDto) {
		if (requestDto.getBranchId() == null) {
			throw new InvalidRequestException("Branch ID is required");
		}
		if (requestDto.getBranchName() == null || requestDto.getBranchName().trim().isEmpty()) {
			throw new InvalidRequestException("Branch name is required");
		}
	}

	private void validateUser(TicketRequestDto requestDto) {
		if (requestDto.getUserId() == null) {
			throw new InvalidRequestException("User ID is required");
		}
	}
}
