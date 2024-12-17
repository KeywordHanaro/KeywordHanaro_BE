package com.hana4.keywordhanaro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana4.keywordhanaro.model.entity.transaction.Transaction;

public interface InquiryJpaRepository extends JpaRepository<Transaction, Long>, InquiryCustomRepository {
	Optional<Transaction> findFirstByAccount_AccountNumber(String accountNumber);
}
