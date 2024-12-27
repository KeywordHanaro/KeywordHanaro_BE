package com.hana4.keywordhanaro.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.dto.AccountResponseDto;
import com.hana4.keywordhanaro.model.mapper.AccountMapper;
import com.hana4.keywordhanaro.model.mapper.AccountResponseMapper;
import com.hana4.keywordhanaro.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
	private final TransactionRepository transactionRepository;

	public List<AccountResponseDto> getRecentTransactionAccounts(Long accountId) throws AccountNotFoundException {
		if (accountId == null) {
			throw new AccountNotFoundException("cannot find account by id");
		}
		return transactionRepository.findRecentTransactionAccounts(
			accountId,
			PageRequest.of(0, 5)
		).stream().map(AccountResponseMapper::toDto).toList();
	}
}
