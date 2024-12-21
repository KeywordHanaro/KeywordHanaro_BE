package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.TransactionDto;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;

public class TransactionMapper {
	public static TransactionDto toDto(Transaction transaction) {
		if (transaction == null) {
			return null;
		}

		return TransactionDto.builder()
			.id(transaction.getId())
			.account(AccountMapper.toDto(transaction.getAccount()))
			.subAccount(AccountMapper.toDto(transaction.getSubAccount()))
			.amount(transaction.getAmount())
			.type(transaction.getType())
			.alias(transaction.getAlias())
			.beforeBalance(transaction.getBeforeBalance())
			.afterBalance(transaction.getAfterBalance())
			.createAt(transaction.getCreateAt())
			.build();
	}
}
