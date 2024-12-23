package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class AccountResponseDto {
	private Long id;
	private String accountNumber;
	private UserResponseDto user;
	private Bank bank;
	private String name;
	private BigDecimal balance;
	private BigDecimal transferLimit;
	private AccountType type;
	private AccountStatus status;

	public AccountResponseDto(Long id, String accountNumber, String name, BigDecimal balance) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.name = name;
		this.balance = balance;
	}

	public AccountResponseDto(String accountNumber, Bank bank) {
		this.accountNumber = accountNumber;
		this.bank = bank;
	}
}
