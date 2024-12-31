package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class AccountDto {
	private Long id;
	private String accountNumber;
	private UserResponseDto user;
	private Bank bank;
	private String name;
	private String password;
	private BigDecimal balance;
	private BigDecimal transferLimit;
	private AccountType type;
	private AccountStatus status;

	public AccountDto(Long id, String accountNumber, String name, BigDecimal balance) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.name = name;
		this.balance = balance;
	}

	public AccountDto(String accountNumber, String password) {
		this.accountNumber = accountNumber;
		this.password = password;
	}

	public AccountDto(String accountNumber, Bank bank) {
		this.accountNumber = accountNumber;
		this.bank = bank;
	}
}
