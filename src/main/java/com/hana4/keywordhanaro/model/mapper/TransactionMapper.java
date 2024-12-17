package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.TransactionDTO;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;

public class TransactionMapper {
	public static TransactionDTO toDto(Transaction transaction) {
		return TransactionDTO.builder()
			.id(transaction.getId())
			.account(AccountMapper.toDTO(transaction.getAccount()))
			.subAccount(AccountMapper.toDTO(transaction.getSubAccount()))
			.amount(transaction.getAmount())
			.type(transaction.getType())
			.alias(transaction.getAlias())
			.beforeBalance(transaction.getBeforeBalance())
			.afterBalance(transaction.getAfterBalance())
			.createAt(transaction.getCreateAt())
			.build();
	}
}
