package com.hana4.keywordhanaro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionStatus;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionType;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.entity.user.UserStatus;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.BankRepository;
import com.hana4.keywordhanaro.repository.InquiryJpaRepository;
import com.hana4.keywordhanaro.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser(username = "admin")
class InquiryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private InquiryJpaRepository inquiryJpaRepository;

	@Autowired
	private BankRepository bankRepository;

	@BeforeAll
	void beforeAll() {
		if (userRepository.findFirstByUsername("seoaLoginID").isEmpty()) {
			User seoaUser = new User("seoaLoginID", "password123", "문서아", UserStatus.ACTIVE, 0);
			userRepository.save(seoaUser);
		}
		if (userRepository.findFirstByUsername("jongwonKing").isEmpty()) {
			User jongwonUser = new User("jongwonKing", "password123", "백종원", UserStatus.ACTIVE, 0);
			userRepository.save(jongwonUser);
		}

		if (accountRepository.findByAccountNumber("123-456-789") == null) {
			User seoaUser = userRepository.findFirstByUsername("seoaLoginID").orElseThrow();
			Account seoaAccount = new Account("123-456-789", seoaUser,
				bankRepository.findAll().stream().findFirst().get(), "식비계좌", "1234", BigDecimal.valueOf(0),
				BigDecimal.valueOf(100000), AccountType.DEPOSIT,
				AccountStatus.ACTIVE);
			accountRepository.save(seoaAccount);
		}
		if (accountRepository.findByAccountNumber("987-654-321") == null) {
			User jongwonUser = userRepository.findFirstByUsername("jongwonKing").orElseThrow();
			Account jongwonAccount = new Account("987-654-321", jongwonUser,
				bankRepository.findAll().stream().findFirst().get(), "자유입출금계좌", "1234", BigDecimal.valueOf(0),
				BigDecimal.valueOf(100000), AccountType.DEPOSIT,
				AccountStatus.ACTIVE);
			accountRepository.save(jongwonAccount);
		}

		if (inquiryJpaRepository.findFirstByAccount_AccountNumber("123-456-789").isEmpty()
			&& inquiryJpaRepository.findFirstByAccount_AccountNumber("987-654-321").isEmpty()) {
			Account seoaAccount = accountRepository.findByAccountNumber("123-456-789")
				.orElseThrow(() -> new NullPointerException("해당 계좌번호가 존재하지 않습니다."));
			Account jongwonAccount = accountRepository.findByAccountNumber("987-654-321")
				.orElseThrow(() -> new NullPointerException("해당 계좌번호가 존재하지 않습니다."));

			// 밥값 거래
			Transaction t1 = new Transaction(seoaAccount, jongwonAccount, BigDecimal.valueOf(20000.0),
				TransactionType.WITHDRAW, "밥값", BigDecimal.valueOf(50000.0), BigDecimal.valueOf(30000.0),
				TransactionStatus.SUCCESS,
				LocalDateTime.now().minusDays(5), null);
			Transaction t2 = new Transaction(jongwonAccount, seoaAccount, BigDecimal.valueOf(20000.0),
				TransactionType.DEPOSIT, "밥값", BigDecimal.valueOf(0.0), BigDecimal.valueOf(20000.0),
				TransactionStatus.SUCCESS,
				LocalDateTime.now().minusDays(5), null);

			// 환불 거래
			Transaction t3 = new Transaction(jongwonAccount, seoaAccount, BigDecimal.valueOf(2000.0),
				TransactionType.WITHDRAW, "환불", BigDecimal.valueOf(20000.0), BigDecimal.valueOf(18000.0),
				TransactionStatus.SUCCESS,
				LocalDateTime.now().minusDays(2), null);
			Transaction t4 = new Transaction(seoaAccount, jongwonAccount, BigDecimal.valueOf(2000.0),
				TransactionType.DEPOSIT, "환불", BigDecimal.valueOf(30000.0), BigDecimal.valueOf(32000.0),
				TransactionStatus.SUCCESS,
				LocalDateTime.now().minusDays(2), null);

			inquiryJpaRepository.saveAll(Arrays.asList(t1, t2, t3, t4));

			seoaAccount.setBalance(BigDecimal.valueOf(32000.0));
			jongwonAccount.setBalance(BigDecimal.valueOf(18000.0));
			accountRepository.saveAll(Arrays.asList(seoaAccount, jongwonAccount));
		}
	}

	@Test
	@Order(1)
	void testGetAccountTransactions() throws Exception {
		String accountNumber = "123-456-789";
		Account account = accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new NullPointerException("해당 계좌번호가 존재하지 않습니다."));
		System.out.println("account = " + account);
		Long accountId = account.getId();
		System.out.println("accountId = " + accountId);
		LocalDate startDate = LocalDate.now().minusDays(30);
		System.out.println("startDate = " + startDate);
		LocalDate endDate = LocalDate.now();
		System.out.println("endDate = " + endDate);

		mockMvc.perform(get("/inquiry/{accountId}", accountId)
				.param("startDate", startDate.toString())
				.param("endDate", endDate.toString())
				.param("transactionType", "all")
				.param("sortOrder", "latest")
				.param("searchWord", ""))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].amount").value(2000))
			.andExpect(jsonPath("$[0].type").value("DEPOSIT"))
			.andExpect(jsonPath("$[1].amount").value(20000))
			.andExpect(jsonPath("$[1].type").value("WITHDRAW"));
	}

	@Test
	@Order(2)
	void testGetAccountTransactionsWithFilter() throws Exception {
		String accountNumber = "123-456-789";
		Account account = accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new NullPointerException("해당 계좌번호가 존재하지 않습니다."));
		Long accountId = account.getId();
		LocalDate startDate = LocalDate.now().minusDays(30);
		LocalDate endDate = LocalDate.now();

		mockMvc.perform(get("/inquiry/{accountId}", accountId)
				.param("startDate", startDate.toString())
				.param("endDate", endDate.toString())
				.param("transactionType", "DEPOSIT")
				.param("sortOrder", "oldest")
				.param("searchWord", "환불"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.length()").value(1))
			.andExpect(jsonPath("$[0].amount").value(2000))
			.andExpect(jsonPath("$[0].type").value("DEPOSIT"))
			.andExpect(jsonPath("$[0].alias").value("환불"));
	}

	@Test
	@Order(3)
	void testGetAccountTransactionsWithSearchWord2() throws Exception {
		String accountNumber = "987-654-321";
		Account account = accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new NullPointerException("해당 계좌번호가 존재하지 않습니다."));
		;
		Long accountId = account.getId();
		LocalDate startDate = LocalDate.now().minusDays(6);
		LocalDate endDate = LocalDate.now();

		mockMvc.perform(get("/inquiry/{accountId}", accountId)
				.param("startDate", startDate.toString())
				.param("endDate", endDate.toString())
				.param("transactionType", "all")
				.param("sortOrder", "latest")
				.param("searchWord", "밥값"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.length()").value(1))
			.andExpect(jsonPath("$[0].amount").value(20000))
			.andExpect(jsonPath("$[0].type").value("DEPOSIT"))
			.andExpect(jsonPath("$[0].alias").value("밥값"));
	}

	@Test
	@Order(4)
	void testGetAccountTransactionsSortOrder() throws Exception {
		String accountNumber = "123-456-789";
		Account account = accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new NullPointerException("해당 계좌번호가 존재하지 않습니다."));
		Long accountId = account.getId();
		LocalDate startDate = LocalDate.now().minusDays(30);
		LocalDate endDate = LocalDate.now();

		// latest
		mockMvc.perform(get("/inquiry/{accountId}", accountId)
				.param("startDate", startDate.toString())
				.param("endDate", endDate.toString())
				.param("transactionType", "all")
				.param("sortOrder", "latest")
				.param("searchWord", ""))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].createAt").isNotEmpty())
			.andExpect(result -> {
				String createdAt0 = JsonPath.parse(result.getResponse().getContentAsString()).read("$[0].createAt");
				String createdAt1 = JsonPath.parse(result.getResponse().getContentAsString()).read("$[1].createAt");
				assertTrue(createdAt0.compareTo(createdAt1) >= 0);
			});

		// oldest
		mockMvc.perform(get("/inquiry/{accountId}", accountId)
				.param("startDate", startDate.toString())
				.param("endDate", endDate.toString())
				.param("transactionType", "all")
				.param("sortOrder", "oldest")
				.param("searchWord", ""))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].createAt").isNotEmpty())
			.andExpect(result -> {
				String createdAt0 = JsonPath.parse(result.getResponse().getContentAsString()).read("$[0].createAt");
				String createdAt1 = JsonPath.parse(result.getResponse().getContentAsString()).read("$[1].createAt");
				assertTrue(createdAt0.compareTo(createdAt1) <= 0);
			});
	}

	@Test
	@Order(5)
	void testInvalidDateFormat() throws Exception {
		String accountNumber = "123-456-789";
		Account account = accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new NullPointerException("해당 계좌번호가 존재하지 않습니다."));
		Long accountId = account.getId();

		mockMvc.perform(get("/inquiry/{accountId}", accountId)
				.param("startDate", "2023/01/01")
				.param("endDate", "2023/12/31")
				.param("transactionType", "all")
				.param("sortOrder", "latest")
				.param("searchWord", ""))
			.andExpect(status().isBadRequest())
			.andExpect(
				jsonPath("$.message").value("Invalid date format. Please provide valid date format (yyyy-MM-dd)."));
	}

	@Test
	@Order(6)
	void testInvalidDateRange() throws Exception {
		String accountNumber = "123-456-789";
		Account account = accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new NullPointerException("해당 계좌번호가 존재하지 않습니다."));
		Long accountId = account.getId();

		mockMvc.perform(get("/inquiry/{accountId}", accountId)
				.param("startDate", "2023-12-31")
				.param("endDate", "2023-01-01")
				.param("transactionType", "all")
				.param("sortOrder", "latest")
				.param("searchWord", ""))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Start date cannot be after end date."));
	}

	@Test
	@Order(7)
	void testInvalidSortOrder() throws Exception {
		String accountNumber = "123-456-789";
		Account account = accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new NullPointerException("해당 계좌번호가 존재하지 않습니다."));
		Long accountId = account.getId();

		mockMvc.perform(get("/inquiry/{accountId}", accountId)
				.param("startDate", "2023-01-01")
				.param("endDate", "2023-12-31")
				.param("transactionType", "all")
				.param("sortOrder", "invalid")
				.param("searchWord", ""))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Invalid sortOrder. Must be 'latest' or 'oldest'."));
	}

	@Test
	@Order(8)
	void testInvalidTransactionType() throws Exception {
		String accountNumber = "123-456-789";
		Account account = accountRepository.findByAccountNumber(accountNumber)
			.orElseThrow(() -> new NullPointerException("해당 계좌번호가 존재하지 않습니다."));
		Long accountId = account.getId();

		mockMvc.perform(get("/inquiry/{accountId}", accountId)
				.param("startDate", "2023-01-01")
				.param("endDate", "2023-12-31")
				.param("transactionType", "invalid")
				.param("sortOrder", "latest")
				.param("searchWord", ""))
			.andExpect(status().isBadRequest())
			.andExpect(
				jsonPath("$.message").value("Invalid transactionType. Must be 'all' or a valid TransactionType."));
	}

	@Test
	@Order(9)
	void testNonExistentAccount() throws Exception {
		Long nonExistentAccountId = 0L;

		mockMvc.perform(get("/inquiry/{accountId}", nonExistentAccountId)
				.param("startDate", "2023-01-01")
				.param("endDate", "2023-12-31")
				.param("transactionType", "all")
				.param("sortOrder", "latest")
				.param("searchWord", ""))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("Account not found with ID: " + nonExistentAccountId));
	}
}
