package com.hana4.keywordhanaro.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.exception.UserNotFoundException;
import com.hana4.keywordhanaro.model.dto.TicketDto;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;
import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.model.entity.Ticket;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.mapper.TicketMapper;
import com.hana4.keywordhanaro.repository.KeywordRepository;
import com.hana4.keywordhanaro.repository.TicketRepository;
import com.hana4.keywordhanaro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {
	private final TicketRepository ticketRepository;
	private final KeywordRepository keywordRepository;
	private final UserRepository userRepository;
	private final Random random = new Random();

	@Override
	public TicketDto createTicket(TicketRequestDto ticketRequestDto, UserDto userDto) throws Exception {
		Long waitingNumber = (long)(random.nextInt(300) + 1); // 1 ~ 300
		Long waitingGuest = (long)(random.nextInt(10) + 1); // 1 ~ 10

		Ticket ticket;

		validateWorkNumber(ticketRequestDto);
		validateBranchIdAndName(ticketRequestDto);

		User findUser = userRepository.findById(userDto.getId())
			.orElseThrow(() -> new UserNotFoundException("User not found"));

		Optional<Ticket> checkTicket = ticketRepository.findByUser(findUser);
		if (checkTicket.isPresent()) {
			return TicketMapper.toDto(checkTicket.get());
		}

		ticket = TicketMapper.toEntity(findUser, ticketRequestDto, waitingNumber, waitingGuest);

		ticket = ticketRepository.save(ticket);
		return TicketMapper.toDto(ticket);
	}

	@Override
	public void updatePermission(Short location, UserDto userDto) throws UserNotFoundException {
		if (location == null || location != 1) {
			throw new InvalidRequestException("Invalid location value");
		}
		User user = userRepository.findById(userDto.getId())
			.orElseThrow(() -> new UserNotFoundException("User not found"));

		user.setPermission(location);
		userRepository.save(user);
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
	
}
