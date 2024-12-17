package com.hana4.keywordhanaro;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.entity.user.UserStatus;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.BankRepository;
import com.hana4.keywordhanaro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitialDataLoader implements ApplicationRunner {
	private final UserRepository userRepository;
	private final BankRepository bankRepository;
	private final AccountRepository accountRepository;

	List<User> userList = new ArrayList<>();
	List<Account> accountList = new ArrayList<>();

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Bank bank = null;
		if (bankRepository.count() == 0) {
			bankRepository.save(new Bank("신한은행"));
			bank = bankRepository.save(new Bank("하나은행"));
			bankRepository.save(new Bank("국민은행"));
		} else {
			bank = bankRepository.findAll().stream().findFirst().get();
		}

		if (userRepository.count() == 0) {
			for (int i = 1; i < 8; i++) {
				userList.add(
					userRepository.save(new User("username" + i, "password" + i, "이름" + i, UserStatus.ACTIVE, 0)));
			}
		} else {
			userList.addAll(userRepository.findAll());
		}

		if (accountRepository.count() == 0) {
			for (int i = 1; i < 8; i++) {
				accountList.add((accountRepository.save(
					new Account("111-222-333" + i, userList.get(i - 1), bank, "계좌" + i,"1234",
						new BigDecimal(9_199_999_999L).setScale(2, RoundingMode.HALF_UP),
						new BigDecimal(9_199_999_999L).setScale(2, RoundingMode.HALF_UP),
						AccountType.DEPOSIT, true, AccountStatus.ACTIVE))));
			}
		} else {
			accountList.addAll(accountRepository.findAll());
		}
	}
}
