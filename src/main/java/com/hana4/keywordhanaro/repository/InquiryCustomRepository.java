package com.hana4.keywordhanaro.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.hana4.keywordhanaro.model.entity.transaction.Transaction;

public interface InquiryCustomRepository {
	List<Transaction> findTransactions(
		Long accountId,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		String transactionType,
		String searchWord,
		String sortOrder
	);
}
