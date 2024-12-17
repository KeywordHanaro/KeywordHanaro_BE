package com.hana4.keywordhanaro.repository;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionType;

@DataJpaTest
@Import(InquiryCustomRepositoryImpl.class)
public class InquiryRepositoryTest {

	@Autowired
	private InquiryJpaRepository inquiryRepository;

	@Autowired
	private AccountRepository accountRepository;

	private Account testAccount;

	@BeforeEach
	void setUp() {
		testAccount = new Account();
		testAccount.setAccountNumber("1234567890");
		testAccount = accountRepository.save(testAccount);

		Transaction transaction1 = new Transaction();
		transaction1.setFromAccount(testAccount);
		transaction1.setToAccount(testAccount);
		transaction1.setAmount(BigDecimal.valueOf(100.0));
		transaction1.setType(TransactionType.valueOf("WITHDRAW"));
		transaction1.setCreateAt(LocalDateTime.now().minusDays(1));
		transaction1.setAlias("Test Transaction 1");

		Transaction transaction2 = new Transaction();
		transaction2.setFromAccount(testAccount);
		transaction2.setToAccount(testAccount);
		transaction2.setAmount(BigDecimal.valueOf(200.0));
		transaction2.setType(TransactionType.valueOf("DEPOSIT"));
		transaction2.setCreateAt(LocalDateTime.now());
		transaction2.setAlias("Test Transaction 2");

		inquiryRepository.saveAll(List.of(transaction1, transaction2));
	}

	@Test
	void testFindTransactions() {
		LocalDateTime startDateTime = LocalDateTime.now().minusDays(2);
		LocalDateTime endDateTime = LocalDateTime.now().plusDays(1);

		List<Transaction> transactions = inquiryRepository.findTransactions(
			testAccount.getId(), startDateTime, endDateTime, "all", null, "DESC");

		assertThat(transactions).isNotEmpty();
		assertThat(transactions).hasSize(2);
		assertThat(transactions.get(0).getAmount()).isEqualTo(200.0);
		assertThat(transactions.get(1).getAmount()).isEqualTo(100.0);
	}

	@Test
	void testFindTransactionsWithTypeFilter() {
		LocalDateTime startDateTime = LocalDateTime.now().minusDays(2);
		LocalDateTime endDateTime = LocalDateTime.now().plusDays(1);

		List<Transaction> transactions = inquiryRepository.findTransactions(
			testAccount.getId(), startDateTime, endDateTime, "TRANSFER", null, "DESC");

		assertThat(transactions).isNotEmpty();
		assertThat(transactions).hasSize(1);
		assertThat(transactions.get(0).getAmount()).isEqualTo(100.0);
	}

	@Test
	void testFindTransactionsWithSearchWord() {
		LocalDateTime startDateTime = LocalDateTime.now().minusDays(2);
		LocalDateTime endDateTime = LocalDateTime.now().plusDays(1);

		List<Transaction> transactions = inquiryRepository.findTransactions(
			testAccount.getId(), startDateTime, endDateTime, "all", "Test Transaction 2", "DESC");

		assertThat(transactions).isNotEmpty();
		assertThat(transactions).hasSize(1);
		assertThat(transactions.get(0).getAmount()).isEqualTo(200.0);
	}
}
