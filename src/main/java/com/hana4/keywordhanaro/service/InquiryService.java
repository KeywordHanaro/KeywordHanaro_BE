package com.hana4.keywordhanaro.service;

import java.time.LocalDate;
import java.util.List;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.model.dto.TransactionDto;

public interface InquiryService {
	List<TransactionDto> getAccountTransactions(Long accountId, LocalDate startDate, LocalDate endDate,
		String transactionType, String sortOrder, String searchWord) throws AccountNotFoundException;
}
