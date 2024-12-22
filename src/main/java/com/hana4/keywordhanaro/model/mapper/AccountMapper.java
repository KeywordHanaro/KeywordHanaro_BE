package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.entity.account.Account;

public class AccountMapper {
	public static AccountDto toDto(Account account) {
		if (account == null) {
			return null;
		}
		return AccountDto.builder()
			.id(account.getId())
			.accountNumber(account.getAccountNumber())
			.user(UserMapper.toDto(account.getUser()))
			.name(account.getName())
			.password(account.getPassword())
			.balance(account.getBalance())
			.transferLimit(account.getTransferLimit())
			.type(account.getType())
			.status(account.getStatus())
			.bank(account.getBank())
			.createAt(account.getCreateAt())
			.updateAt(account.getUpdateAt())
			.build();
	}

	public static Account toEntity(AccountDto accountDto) {
		if (accountDto == null) {
			return null;
		}
		return new Account(accountDto.getId(), accountDto.getAccountNumber(), UserMapper.toEntity(accountDto.getUser()),
			accountDto.getBank(), accountDto.getName(), accountDto.getPassword(), accountDto.getBalance(),
			accountDto.getTransferLimit(), accountDto.getType(), accountDto.getStatus());
	}
}
