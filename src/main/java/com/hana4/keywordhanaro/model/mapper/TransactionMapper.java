package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.TransactionDto;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;

public class TransactionMapper {
	public static TransactionDto toDto(Transaction transaction) {
		return TransactionDto.builder()
			.id(transaction.getId())
			.fromAccount(AccountMapper.toDTO(transaction.getFromAccount()))
			.toAccount(AccountMapper.toDTO(transaction.getToAccount()))
			.amount(transaction.getAmount())
			.type(transaction.getType())
			.alias(transaction.getAlias())
			.beforeBalance(transaction.getBeforeBalance())
			.afterBalance(transaction.getAfterBalance())
			.createdAt(transaction.getCreateAt())
			.build();
	}
}
