package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class AccountDto extends BaseDto {
	private Long id;
	private String accountNumber;
	private UserDto user;
	private Bank bank;
	private String name;
	@JsonIgnore
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
}
