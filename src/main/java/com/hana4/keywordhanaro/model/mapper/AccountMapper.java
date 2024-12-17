package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.entity.account.Account;

public class AccountMapper {
	public static AccountDto toDTO(Account account) {
		return AccountDto.builder()
			.id(account.getId())
			.accountNumber(account.getAccountNumber())
			.userId(account.getUser().getId())
			.name(account.getName())
			.password(account.getPassword())
			.balance(account.getBalance())
			.transferLimit(account.getTransferLimit())
			.type(account.getType())
			.mine(account.getIsMine())
			.status(account.getStatus())
			.bankId(account.getBank().getId())
			.createAt(account.getCreateAt())
			.updateAt(account.getUpdateAt())
			.build();
	}
}
