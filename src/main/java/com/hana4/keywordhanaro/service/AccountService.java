package com.hana4.keywordhanaro.service;

import java.util.List;

import com.hana4.keywordhanaro.model.dto.AccountDTO;

public interface AccountService {
	AccountDTO getAccount(Long id);

	List<AccountDTO> getAccounts();
}
