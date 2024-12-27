package com.hana4.keywordhanaro.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.mapper.AccountMapper;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public AccountDto getAccount(Long id) throws AccountNotFoundException {
		return AccountMapper.toDto(accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("cannot find account by id")));
	}

	@Override
	public List<AccountDto> getAccounts() {
		return accountRepository.findAll().stream().map(AccountMapper::toDto).toList();
	}

	@Override
	public boolean checkPassword(String accountNumber, String password) throws AccountNotFoundException {
		Account account = accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new AccountNotFoundException("cannot find account by accountNumber"));

		return passwordEncoder.matches(password, account.getPassword());
	}

	@Override
	public List<AccountDto> getAccountsByUsername(String username) {
		return accountRepository.findAllByUserUsername(username).stream().map(AccountMapper::toDto).toList();
	}

	@Override
	public String checkAccountNumberAndBank(String accountNumber, Bank bank) throws AccountNotFoundException {
		Account account = accountRepository.findByAccountNumberAndBank(accountNumber, bank)
			.orElseThrow(() -> new AccountNotFoundException("cannot find account by accountNumber"));

		return account.getUser().getName();
	}
}
