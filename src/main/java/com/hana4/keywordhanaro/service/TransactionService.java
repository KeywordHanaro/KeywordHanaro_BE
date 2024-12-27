package com.hana4.keywordhanaro.service;

import java.util.List;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.dto.AccountResponseDto;

public interface TransactionService {
	List<AccountResponseDto> getRecentTransactionAccounts(Long accountId) throws AccountNotFoundException;
}
