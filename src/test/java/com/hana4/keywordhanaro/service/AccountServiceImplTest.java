package com.hana4.keywordhanaro.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hana4.keywordhanaro.repository.AccountRepository;

@SpringBootTest
class AccountServiceImplTest {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;

	@Test
	void checkPasswordTest() {
		Assertions.assertThat(accountService.checkPassword("1231-1231-1231", "1234")).isTrue();
	}

}
