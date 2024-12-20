package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;
import com.hana4.keywordhanaro.model.entity.user.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class AccountDto extends BaseDto {
	private Long id;
	private String accountNumber;
	private User user;
	private Bank bank;
	private String name;
	private String password;
	private BigDecimal balance;
	private BigDecimal transferLimit;
	private AccountType type;
	private Boolean mine;
	private AccountStatus status;

}
