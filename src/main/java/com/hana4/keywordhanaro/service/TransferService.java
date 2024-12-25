package com.hana4.keywordhanaro.service;

import java.math.BigDecimal;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;

public interface TransferService {
	Transaction transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount)
		throws AccountNotFoundException, Exception;
}
