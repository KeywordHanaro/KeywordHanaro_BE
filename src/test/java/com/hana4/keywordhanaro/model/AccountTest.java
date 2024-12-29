package com.hana4.keywordhanaro.model;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;
import com.hana4.keywordhanaro.model.entity.user.User;

public class AccountTest {
	@Test
	void accountTest() {
		Account account = new Account("xx", new User(), new Bank(), "", "", new BigDecimal(1), new BigDecimal(1),
			AccountType.DEPOSIT, AccountStatus.ACTIVE);
		Account account2 = new Account("xx", new User(), new Bank(), "", "", new BigDecimal(1), new BigDecimal(1),
			AccountType.DEPOSIT, AccountStatus.ACTIVE);

		Assertions.assertThat(account).isEqualTo(account2);

		Account account3 = new Account(1L, "", new User(), new Bank(), "", BigDecimal.TEN, BigDecimal.TEN,
			AccountType.DEPOSIT, AccountStatus.ACTIVE);

		Account account4 = new Account(1L, "", new User(), new Bank(), "", BigDecimal.TEN, BigDecimal.TEN,
			AccountType.DEPOSIT, AccountStatus.ACTIVE);

		Assertions.assertThat(account3).isEqualTo(account4);

	}
}
