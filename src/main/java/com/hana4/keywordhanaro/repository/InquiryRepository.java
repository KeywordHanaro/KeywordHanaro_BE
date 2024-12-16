package com.hana4.keywordhanaro.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hana4.keywordhanaro.model.entity.transaction.Transaction;

public interface InquiryRepository extends JpaRepository<Transaction, Long> {
	@Query("SELECT t FROM Transaction t "
		+ "WHERE (t.fromAccount.id = :accountId OR t.toAccount.id = :accountId) "
		+ "AND t.createAt BETWEEN :startDateTime AND :endDateTime "
		+ "AND (:transactionType = 'all' OR t.type = :transactionType) "
		+ "AND (:searchWord IS NULL OR LOWER(t.alias) LIKE LOWER(CONCAT('%', :searchWord, '%'))) "
		+ "ORDER BY t.createAt :sortOrder")
	List<Transaction> findTransactions(
		@Param("accountId") Long accountId,
		@Param("startDateTime") LocalDateTime startDateTime,
		@Param("endDateTime") LocalDateTime endDateTime,
		@Param("transactionType") String transactionType,
		@Param("searchWord") String searchWord,
		@Param("sortOrder") String sortOrder
	);
}
