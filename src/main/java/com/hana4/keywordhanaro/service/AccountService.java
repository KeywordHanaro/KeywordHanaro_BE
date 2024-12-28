package com.hana4.keywordhanaro.service;

import java.util.List;
import java.util.Map;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.entity.Bank;

public interface AccountService {
	AccountDto getAccount(Long id) throws AccountNotFoundException;

	List<AccountDto> getAccounts();

	boolean checkPassword(String accountNumber, String password) throws AccountNotFoundException;

	List<AccountDto> getAccountsByUsername(String username);

	Map<String, Object> checkAccountNumberAndBank(String accountNumber, Short bankId) throws AccountNotFoundException;
}
