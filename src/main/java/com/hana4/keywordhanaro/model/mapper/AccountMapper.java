package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.entity.account.Account;

public class AccountMapper {
	public static AccountDto toDTO(Account account) {
		if (account == null) {
			return null;
		}
		return AccountDto.builder()
			.id(account.getId())
			.accountNumber(account.getAccountNumber())
			.user(account.getUser())
			.name(account.getName())
			.password(account.getPassword())
			.balance(account.getBalance())
			.transferLimit(account.getTransferLimit())
			.type(account.getType())
			.mine(account.getIsMine())
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
		return new Account(accountDto.getId(), accountDto.getAccountNumber(), accountDto.getUser(),
			accountDto.getBank(), accountDto.getName(), accountDto.getPassword(), accountDto.getBalance(),
			accountDto.getTransferLimit(), accountDto.getType(), accountDto.getMine(), accountDto.getStatus());
	}
}
