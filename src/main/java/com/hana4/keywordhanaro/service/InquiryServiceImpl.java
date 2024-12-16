package com.hana4.keywordhanaro.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.model.dto.TransactionDTO;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.mapper.TransactionMapper;
import com.hana4.keywordhanaro.repository.InquiryRepository;

@Service
public class InquiryServiceImpl implements InquiryService {
	public final InquiryRepository inquiryRepository;

	public InquiryServiceImpl(InquiryRepository inquiryRepository) {
		this.inquiryRepository = inquiryRepository;
	}

	@Override
	public List<TransactionDTO> getAccountTransactions(Long accountId, LocalDate startDate, LocalDate endDate,
		String transactionType, String sortOrder, String searchWord) {
		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

		String dbSortOrder = sortOrder.equalsIgnoreCase("latest") ? "DESC" : "ASC";

		List<Transaction> transactions = inquiryRepository.findTransactions(
			accountId, startDateTime, endDateTime, transactionType, searchWord, dbSortOrder
		);

		return transactions.stream()
			.map(TransactionMapper::toDto)
			.toList();
	}
}
