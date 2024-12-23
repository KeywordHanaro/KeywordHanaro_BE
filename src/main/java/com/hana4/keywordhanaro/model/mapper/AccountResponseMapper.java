package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.AccountResponseDto;
import com.hana4.keywordhanaro.model.entity.account.Account;

public class AccountResponseMapper {
	public static AccountResponseDto toDto(Account account) {
		if (account == null) {
			return null;
		}
		return AccountResponseDto.builder()
			.id(account.getId())
			.accountNumber(account.getAccountNumber())
			.user(UserResponseMapper.toDto(account.getUser()))
			.name(account.getName())
			.balance(account.getBalance())
			.transferLimit(account.getTransferLimit())
			.type(account.getType())
			.status(account.getStatus())
			.bank(account.getBank())
			.build();
	}

	public static Account toEntity(AccountResponseDto accountDto) {
		if (accountDto == null) {
			return null;
		}
		return new Account(accountDto.getId(), accountDto.getAccountNumber(),
			UserResponseMapper.toEntity(accountDto.getUser()),
			accountDto.getBank(), accountDto.getName(), accountDto.getBalance(),
			accountDto.getTransferLimit(), accountDto.getType(), accountDto.getStatus());
	}
}