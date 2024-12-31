package com.hana4.keywordhanaro.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.model.dto.TransactionDto;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionType;
import com.hana4.keywordhanaro.model.mapper.TransactionMapper;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.InquiryJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryServiceImpl implements InquiryService {
	public final InquiryJpaRepository inquiryRepository;
	public final AccountRepository accountRepository;

	@Override
	public List<TransactionDto> getAccountTransactions(Long accountId, LocalDate startDate, LocalDate endDate,
		String transactionType, String sortOrder, String searchWord) throws AccountNotFoundException {
		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

		validateRequest(startDate, endDate, transactionType, sortOrder);

		String dbSortOrder = sortOrder.equalsIgnoreCase("latest") ? "DESC" : "ASC";

		accountRepository.findById(accountId)
			.orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));

		List<Transaction> transactions = inquiryRepository.findTransactions(
			accountId, startDateTime, endDateTime, transactionType, searchWord, dbSortOrder
		);

		return transactions.stream()
			.map(TransactionMapper::toDto)
			.toList();
	}

	private void validateRequest(LocalDate startDate, LocalDate endDate, String transactionType, String sortOrder) {
		validateDateRange(startDate, endDate);
		validateSortOrder(sortOrder);
		validateTransactionType(transactionType);
	}

	private void validateDateRange(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null) {
			throw new InvalidRequestException("Start date and end date cannot be null");
		}

		if (startDate.isAfter(endDate)) {
			log.error("Invalid date range: startDate={}, endDate={}", startDate, endDate);
			throw new InvalidRequestException("Start date cannot be after end date.");
		}
	}

	private void validateSortOrder(String sortOrder) {
		if (sortOrder == null || (!sortOrder.equalsIgnoreCase("latest") && !sortOrder.equalsIgnoreCase("oldest"))) {
			log.error("Invalid sortOrder: {}", sortOrder);
			throw new InvalidRequestException("Invalid sortOrder. Must be 'latest' or 'oldest'.");
		}
	}

	private void validateTransactionType(String transactionType) {
		if (transactionType == null || !transactionType.equalsIgnoreCase("all") && !isValidTransactionType(
			transactionType)) {
			log.error("Invalid transactionType: {}", transactionType);
			throw new InvalidRequestException("Invalid transactionType. Must be 'all' or a valid TransactionType.");
		}
	}

	private boolean isValidTransactionType(String transactionType) {
		try {
			TransactionType.valueOf(transactionType.toUpperCase());
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
