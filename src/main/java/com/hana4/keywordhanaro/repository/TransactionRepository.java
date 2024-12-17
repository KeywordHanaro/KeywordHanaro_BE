package com.hana4.keywordhanaro.repository;

import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
