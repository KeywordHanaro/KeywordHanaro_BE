package com.hana4.keywordhanaro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionStatus;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findByTypeAndStatus(TransactionType transactionType, TransactionStatus transactionStatus);

	List<Transaction> findByAccount(Account account);
}
