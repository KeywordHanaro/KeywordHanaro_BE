package com.hana4.keywordhanaro.service;

import java.util.List;

import com.hana4.keywordhanaro.model.dto.AccountDto;

public interface AccountService {
	AccountDto getAccount(Long id);

	List<AccountDto> getAccounts();

	boolean checkPassword(String accountNumber, String password);
}
