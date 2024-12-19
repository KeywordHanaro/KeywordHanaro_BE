package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Data
@NoArgsConstructor
public class AccountDto extends BaseDto {
	private Long id;
	private String accountNumber;
	private String userId;
	private Short bankId;
	private String name;
	private String password;
	private BigDecimal balance;
	private BigDecimal transferLimit;
	private AccountType type;
	private Boolean mine;
	private AccountStatus status;

	public AccountDto(Long id) {
		this.id = id;
	}
}
