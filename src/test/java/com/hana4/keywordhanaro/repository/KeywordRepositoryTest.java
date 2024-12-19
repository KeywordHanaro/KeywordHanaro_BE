package com.hana4.keywordhanaro.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.entity.user.UserStatus;

import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KeywordRepositoryTest {
	@Autowired
	private KeywordRepository keywordRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BankRepository bankRepository;

	@Autowired
	private EntityManager em;

	@BeforeAll
	void beforeAll() {
		if (userRepository.findFirstByUsername("insunID").isEmpty()) {
			User inssUser = new User("insunID", "insss123", "김인선", UserStatus.ACTIVE, 0);
			userRepository.save(inssUser);
		}

		if (accountRepository.findByAccountNumber("111-222-3342").isEmpty()) {
			User inssUser = userRepository.findFirstByUsername("insunID")
				.orElseThrow(() -> new NullPointerException("User not found"));
			Bank bank = bankRepository.findAll().stream().findFirst().get();
			Account inssAccount = new Account("111-222-3342", inssUser, bank, "생활비 계좌", "1234", BigDecimal.valueOf(0),
				BigDecimal.valueOf(300000), AccountType.DEPOSIT,
				true, AccountStatus.ACTIVE);
			accountRepository.save(inssAccount);
		}

	}

	@Test
	@DisplayName("키워드 삭제 테스트")
	void deleteKeywordTest() {
		Keyword keyword = new Keyword();
		User user = new User();
		user.setId("2160a26a-0a23-4a16-813b-9045b2a5b489");
		keyword.setUser(user);
		keyword.setType(KeywordType.TICKET);
		keyword.setName("keyword");
		keyword.setDescription("keyword description");
		keyword.setSeqOrder(100L);
		keyword.setBranch("branch");
		em.persist(keyword);
		Keyword saveKeyword = keywordRepository.save(keyword);

		keywordRepository.delete(saveKeyword);

		Optional<Keyword> deletedKeyword = keywordRepository.findById(saveKeyword.getId());
		assertThat(deletedKeyword).isEmpty();
	}

	@Test
	@DisplayName("키워드 생성 테스트")
	void createKeywordTest() {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new NullPointerException("User not found"));
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new NullPointerException("Account not found"));

		Keyword keyword = new Keyword(testUser, KeywordType.INQUIRY, "월급 조회", "자유입출금계좌에서 조회 > 월급", 100L, testAccount,
			"급여");

		Keyword savedKeyword = keywordRepository.save(keyword);
		assertNotNull(savedKeyword);
		assertEquals("월급 조회", savedKeyword.getName());
		assertEquals(KeywordType.INQUIRY, savedKeyword.getType());
		assertEquals("자유입출금계좌에서 조회 > 월급", savedKeyword.getDescription());
		assertEquals(100L, savedKeyword.getSeqOrder());
		assertEquals("급여", savedKeyword.getInquiryWord());
		assertEquals(testUser.getId(), savedKeyword.getUser().getId());
		assertEquals(testAccount.getId(), savedKeyword.getAccount().getId());
	}

}
