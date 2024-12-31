package com.hana4.keywordhanaro.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionStatus;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findByTypeAndStatus(TransactionType transactionType, TransactionStatus transactionStatus);

	List<Transaction> findByAccount(Account account);

	@Query("SELECT DISTINCT t.subAccount " +
		"FROM Transaction t " +
		"WHERE t.account.id = :accountId " +
		"AND t.subAccount IS NOT NULL " +
		"AND t.status = 'SUCCESS' " +
		"AND t.subAccount.user.id <> t.account.user.id " +
		"GROUP BY t.subAccount " +
		"ORDER BY MAX(t.createAt) DESC")
	List<Account> findRecentTransactionAccounts(
		@Param("accountId") Long accountId,
		Pageable pageable);
}
