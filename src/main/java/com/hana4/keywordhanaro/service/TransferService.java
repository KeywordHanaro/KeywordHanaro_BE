package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.model.entity.transaction.Transaction;

import java.math.BigDecimal;

public interface TransferService {
    Transaction transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount);

    double getInitialBalance();
}
