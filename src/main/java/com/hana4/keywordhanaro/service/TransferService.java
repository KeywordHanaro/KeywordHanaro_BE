package com.hana4.keywordhanaro.service;

import java.math.BigDecimal;

public interface TransferService {
    void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount);

    double getInitialBalance();
}
