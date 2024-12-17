package com.hana4.keywordhanaro.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.mapper.AccountMapper;
import com.hana4.keywordhanaro.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	@Override
	public AccountDto getAccount(Long id) {
		return AccountMapper.toDTO(Objects.requireNonNull(accountRepository.findById(id).orElse(null)));
	}

	@Override
	public List<AccountDto> getAccounts() {
		return accountRepository.findAll().stream().map(AccountMapper::toDTO).toList();
	}
}
