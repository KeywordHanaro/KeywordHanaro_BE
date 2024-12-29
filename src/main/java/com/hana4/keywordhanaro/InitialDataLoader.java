package com.hana4.keywordhanaro;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// if (bankRepository.findAll().size() == 0) {
		// 	Bank[] banks = {
		// 		new Bank((short) 2, "산업은행"),
		// 		new Bank((short) 3, "기업은행"),
		// 		new Bank((short) 4, "국민은행"),
		// 		new Bank((short) 7, "수협은행"),
		// 		new Bank((short) 11, "농협은행"),
		// 		new Bank((short) 20, "우리은행"),
		// 		new Bank((short) 23, "SC은행"),
		// 		new Bank((short) 27, "씨티은행"),
		// 		new Bank((short) 31, "대구은행"),
		// 		new Bank((short) 32, "부산은행"),
		// 		new Bank((short) 34, "광주은행"),
		// 		new Bank((short) 35, "제주은행"),
		// 		new Bank((short) 37, "전북은행"),
		// 		new Bank((short) 39, "경남은행"),
		// 		new Bank((short) 45, "새마을금고"),
		// 		new Bank((short) 48, "신협은행"),
		// 		new Bank((short) 71, "우체국"),
		// 		new Bank((short) 81, "하나은행"),
		// 		new Bank((short) 88, "신한은행"),
		// 		new Bank((short) 89, "K뱅크"),
		// 		new Bank((short) 90, "카카오뱅크"),
		// 		new Bank((short) 92, "토스뱅크"),
		// 		new Bank((short) 103, "SBI저축은행")
		// 	};
		//
		// 	bankRepository.saveAll(List.of(banks));
		// }
		//
		// if (userRepository.findAll().size() == 0) {
		// 	userRepository.save(
		// 		new User("admin1", bCryptPasswordEncoder.encode("1234"), bCryptPasswordEncoder.encode("0000"), "남인우",
		// 			UserStatus.ACTIVE, "admin1@gmail.com", "010-1111-1111", 1, "6N3r3evc5cn4wPfE9sH4z_zQ4dDn0enf5pw"));
		// 	userRepository.save(
		// 		new User("admin2", bCryptPasswordEncoder.encode("1234"), bCryptPasswordEncoder.encode("0000"), "김인선", UserStatus.ACTIVE, "admin2@gmail.com",
		// 			"010-2222-2222", 1, "6Nzr2eHS4dT4yv3F8cb0xPHd7N3q3OTS65w"));
		// 	userRepository.save(
		// 		new User("admin3", bCryptPasswordEncoder.encode("1234"), bCryptPasswordEncoder.encode("0000"), "김도희", UserStatus.ACTIVE, "admin3@gmail.com",
		// 			"010-3333-3333", 1, "6Nnh0-Pa69LhzfnA8cb2zv_M4NHg1-HZ79aq"));
		// 	userRepository.save(
		// 		new User("admin4", bCryptPasswordEncoder.encode("1234"), bCryptPasswordEncoder.encode("0000"), "문서아", UserStatus.ACTIVE, "admin4@gmail.com",
		// 			"010-4444-4444", 1, "6Nvr3-rb4tH9z_7I-cz6zfzQ4dDn0enf5mc"));
		// 	userRepository.save(
		// 		new User("admin5", bCryptPasswordEncoder.encode("1234"), bCryptPasswordEncoder.encode("0000"), "조민석", UserStatus.ACTIVE, "admin5@gmail.com",
		// 			"010-5555-5555", 1, "6Nrt2O_X5NfkyPHI_Mj-yfnN4dDh1uDY7tes"));
		// 	userRepository.save(
		// 		new User("admin6", bCryptPasswordEncoder.encode("1234"), bCryptPasswordEncoder.encode("0000"), "정성엽", UserStatus.ACTIVE, "admin6@gmail.com",
		// 			"010-6666-6666", 1, "6Nrr2u7Y6N7vw_bC8Mj5yPvN4dDh1uDY7tes"));
		// 	userRepository.save(
		// 		new User("admin7", bCryptPasswordEncoder.encode("1234"), bCryptPasswordEncoder.encode("0000"), "박준용", UserStatus.ACTIVE, "admin7@gmail.com",
		// 			"010-7777-7777", 1, "6Nrv3u3Z69zwwfjB-MD3xvbO4tPi1ePb7dSr"));
		// }

		// userRepository.save(
		// 	new User("bapplus", bCryptPasswordEncoder.encode("1234"), "밥플러스", UserStatus.ACTIVE, "bapplus@gmail.com", "010-1234-1234", 0));
		//
		// userRepository.save(
		// 	new User("seongcha1", bCryptPasswordEncoder.encode("1234"), "성수차이나", UserStatus.ACTIVE, "seongcha@gmail.com", "010-1234-5678", 0));

		// userRepository.save(
		// 	new User("samsungcom", bCryptPasswordEncoder.encode("1234"), "삼성전자", UserStatus.ACTIVE, "samsung@gmail.com", "010-1234-5678", 0));

		// User user = userRepository.findById("c4cd30f5-d85f-4d65-8f1d-0986fc62d8de").get();
		// // System.out.println("allUser = " + allUser);
		// List<Bank> allBank = bankRepository.findAll();
		//
		// accountRepository.save(new Account("312-123-223349", user , bank, "달달하나입출금통장",
		// 	bCryptPasswordEncoder.encode("1234"), new BigDecimal(523944), new BigDecimal(100000000),
		// 	AccountType.DEPOSIT, AccountStatus.ACTIVE));

		// Bank hanabank = bankRepository.findById((short)81).get();
		// List<User> allUser = userRepository.findAll();
		//
		// if (accountRepository.findAll().size() == 0) {
		// 	for (User user : allUser) {
		// 		accountRepository.save(
		// 			new Account(generateAccountNumber(), user, hanabank, hanabank.getName() + "입출금계좌",
		// 				bCryptPasswordEncoder.encode("1234"), new BigDecimal(10000000), new BigDecimal(100000000),
		// 				AccountType.DEPOSIT, AccountStatus.ACTIVE));
		//
		// 	}
		// }
		// User user = userRepository.findFirstByUsername("bapplus").get();
		// accountRepository.save(
		// 	new Account(generateAccountNumber(), user, hanabank, "밥플러스", bCryptPasswordEncoder.encode("1234"),
		// 		new BigDecimal("10000"), new BigDecimal(100000000), AccountType.DEPOSIT, AccountStatus.ACTIVE));

		// User user2 = userRepository.findFirstByUsername("seongcha1").get();
		// accountRepository.save(
		// 	new Account(generateAccountNumber(), user2, hanabank, "성수차이나", bCryptPasswordEncoder.encode("1234"),
		// 		new BigDecimal("10000"), new BigDecimal(100000000), AccountType.DEPOSIT, AccountStatus.ACTIVE));

		// User user = userRepository.findFirstByUsername("admin6").get();
		// accountRepository.save(
		// 	new Account(generateAccountNumber(), user, hanabank, "주거래하나", bCryptPasswordEncoder.encode("1234"),
		// 		new BigDecimal("10000"), new BigDecimal(100000000), AccountType.DEPOSIT, AccountStatus.ACTIVE));
		//
		// accountRepository.save(
		// 	new Account(generateAccountNumber(), user, hanabank, "달달하나", bCryptPasswordEncoder.encode("1234"),
		// 		new BigDecimal("10000"), new BigDecimal(100000000), AccountType.DEPOSIT, AccountStatus.ACTIVE));

		// User user = userRepository.findFirstByUsername("admin1").get();
		//
		// accountRepository.save(new Account("123213123", user, bankRepository.findAll().stream().findFirst().get(),
		// 	bankRepository.findAll().stream().findFirst().get().getName(), bCryptPasswordEncoder.encode("1234"),
		// 	new BigDecimal(10000000), new BigDecimal(100000000), AccountType.DEPOSIT, AccountStatus.ACTIVE));

		// User user = userRepository.findFirstByUsername("admin7").get();
		// user.setMasterPassword(bCryptPasswordEncoder.encode("1234"));
		// System.out.println("user.getMasterPassword() = " + user.getMasterPassword());

	}

	// public String generateAccountNumber() {
	// 	Random random = new Random();
	// 	StringBuilder accountNumber = new StringBuilder();
	//
	// 	for (int i = 0; i < 12; i++) {
	// 		accountNumber.append(random.nextInt(10));
	// 	}
	//
	// 	return accountNumber.toString();
	// }
}
