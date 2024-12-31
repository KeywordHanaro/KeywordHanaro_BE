package com.hana4.keywordhanaro.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionStatus;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionType;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.entity.user.UserStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
class InquiryRepositoryTest {

	@Autowired
	private InquiryJpaRepository inquiryJpaRepository;

	@Autowired
	private AccountRepository accountRepository;

	private List<Transaction> testTransactions;

	// EntityManager 주입
	@PersistenceContext
	private EntityManager entityManager;

	@BeforeEach
	void setUp(@Autowired UserRepository userRepository, @Autowired BankRepository bankRepository,
		@Autowired AccountRepository accountRepository) {
		if (userRepository.findFirstByUsername("seoaLoginID").isEmpty()) {
			User seoaUser = new User("seoaLoginID", "password123", "문서아", UserStatus.ACTIVE, 0);
			userRepository.save(seoaUser);
		}
		if (userRepository.findFirstByUsername("jongwonKing").isEmpty()) {
			User jongwonUser = new User("jongwonKing", "password123", "백종원", UserStatus.ACTIVE, 0);
			userRepository.save(jongwonUser);
		}

		if (accountRepository.findByAccountNumber("123-456-789").isEmpty()) {
			User seoaUser = userRepository.findFirstByUsername("seoaLoginID").orElseThrow();
			Account seoaAccount = new Account("123-456-789", seoaUser,
				bankRepository.findAll().stream().findFirst().get(), "식비계좌", "1234", BigDecimal.valueOf(0),
				BigDecimal.valueOf(100000), AccountType.DEPOSIT,
				AccountStatus.ACTIVE);
			accountRepository.save(seoaAccount);
		}
	}

	@AfterEach
	void tearDown() {
		if (testTransactions != null && !testTransactions.isEmpty()) {
			inquiryJpaRepository.deleteAll(testTransactions);
		}
		entityManager.flush();
		entityManager.clear();
	}

	@Test
	@Transactional
		// @RepeatedTest(10)
	void testFindTransactions() {

		Account account = accountRepository.findByAccountNumber("123-456-789")
			.orElseThrow(() -> new NullPointerException("해당 계좌번호가 존재하지 않습니다."));

		Transaction t1 = new Transaction(account, account, BigDecimal.valueOf(20000.0),
			TransactionType.WITHDRAW, "식비", BigDecimal.valueOf(100000.0),
			BigDecimal.valueOf(80000.0),
			TransactionStatus.SUCCESS, null);
		Transaction t2 = new Transaction(account, account, BigDecimal.valueOf(10000.0),
			TransactionType.DEPOSIT, "식사", BigDecimal.valueOf(80000.0),
			BigDecimal.valueOf(90000.0),
			TransactionStatus.SUCCESS,
			null);
		testTransactions = inquiryJpaRepository.saveAll(List.of(t1, t2));

		// flush 후 캐시를 비우기 위해 EntityManager를 사용
		entityManager.flush();
		entityManager.clear();

		LocalDateTime startDateTime = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
		LocalDateTime endDateTime = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
		String transactionType = "all";
		String searchWord = "식사";
		String sortOrder = "desc";

		List<Transaction> transactions = inquiryJpaRepository.findTransactions(
			account.getId(), startDateTime, endDateTime, transactionType, searchWord, sortOrder);

		System.out.println("transactions = " + transactions);

		assertNotNull(transactions);
		assertFalse(transactions.isEmpty());
		assertTrue(transactions.stream().anyMatch(transaction -> transaction.getAlias().contains("식사")));
		assertEquals("식사", transactions.get(0).getAlias());
	}
}
