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
import org.springframework.security.test.context.support.WithMockUser;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.exception.KeywordNotFoundException;
import com.hana4.keywordhanaro.exception.UserNotFoundException;
import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.entity.user.UserStatus;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "insun")
public class KeywordRepositoryTest {
	@Autowired
	private KeywordRepository keywordRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BankRepository bankRepository;

	@BeforeAll
	void beforeAll() throws Exception {
		if (userRepository.findFirstByUsername("insunID").isEmpty()) {
			User inssUser = new User("insunID", "insss123", "김인선", UserStatus.ACTIVE, 0);
			userRepository.save(inssUser);
		}
		if (userRepository.findFirstByUsername("JunYongID").isEmpty()) {
			User junUser = new User("JunYongID", "jun123", "박준용", UserStatus.ACTIVE, 0);
			userRepository.save(junUser);
		}

		if (accountRepository.findByAccountNumber("111-222-3342").isEmpty()) {
			User inssUser = userRepository.findFirstByUsername("insunID")
				.orElseThrow(() -> new UserNotFoundException("User not found"));
			Bank bank = bankRepository.findAll().stream().findFirst().get();
			Account inssAccount = new Account("111-222-3342", inssUser, bank, "생활비 계좌", "1234", BigDecimal.valueOf(0),
				BigDecimal.valueOf(300000), AccountType.DEPOSIT,
				AccountStatus.ACTIVE);
			accountRepository.save(inssAccount);
		}

		if (accountRepository.findByAccountNumber("111-333-3342").isEmpty()) {
			User JunYongUser = userRepository.findFirstByUsername("JunYongID")
				.orElseThrow(() -> new UserNotFoundException("User not found"));
			Bank bank = bankRepository.findAll().stream().findFirst().get();
			Account JunYonAccount = new Account("111-333-3342", JunYongUser, bank, "생활비 계좌", "1234",
				BigDecimal.valueOf(0),
				BigDecimal.valueOf(300000), AccountType.DEPOSIT,
				AccountStatus.ACTIVE);
			accountRepository.save(JunYonAccount);
		}

		User JunYongUser = userRepository.findFirstByUsername("JunYongID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));
		if (keywordRepository.findByUserId(JunYongUser.getId()).isEmpty()) {
			Account inssAccount = accountRepository.findByAccountNumber("111-222-3342")
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));
			Account JunYongAccount = accountRepository.findByAccountNumber("111-333-3342")
				.orElseThrow(() -> new AccountNotFoundException("SubAccount not found"));

			Keyword k1 = new Keyword(JunYongUser, KeywordType.TRANSFER, "성엽이 용돈", "사용 repository 테스트", 200L,
				JunYongAccount,
				inssAccount, BigDecimal.valueOf(50000), false);
			keywordRepository.save(k1);
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
		Keyword saveKeyword = keywordRepository.save(keyword);

		keywordRepository.delete(saveKeyword);

		Optional<Keyword> deletedKeyword = keywordRepository.findById(saveKeyword.getId());
		assertThat(deletedKeyword).isEmpty();
	}

	@Test
	@DisplayName("키워드 생성 테스트")
	void createKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));

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

	@Test
	@DisplayName("조회 키워드 수정 테스트")
	public void updateInquiryKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));
		Account testFromAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		Keyword keyword = new Keyword(testUser, KeywordType.INQUIRY, "title", "description", 100L, testFromAccount,
			"조회어");
		Keyword savedKeyword = keywordRepository.save(keyword);

		savedKeyword.setName("수정된 조회 키워드");
		savedKeyword.setDescription("수정된 조회 설명");
		savedKeyword.setInquiryWord("수정된 조회어");

		Keyword updatedKeyword = keywordRepository.save(savedKeyword);

		assertThat(updatedKeyword.getName()).isEqualTo("수정된 조회 키워드");
		assertThat(updatedKeyword.getDescription()).isEqualTo("수정된 조회 설명");
		assertThat(updatedKeyword.getInquiryWord()).isEqualTo("수정된 조회어");
	}

	@Test
	@DisplayName("송금 키워드 수정 테스트")
	public void updateTransferKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));
		Account testFromAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		Account testToAccount = accountRepository.findByAccountNumber("111-333-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		Keyword keyword = new Keyword(testUser, KeywordType.TRANSFER, "title", "description", 100L, testFromAccount,
			testToAccount, BigDecimal.valueOf(200000), false);
		Keyword savedKeyword = keywordRepository.save(keyword);

		savedKeyword.setName("수정된 이체 키워드");
		savedKeyword.setDescription("수정된 이체 설명");
		savedKeyword.setAmount(BigDecimal.valueOf(100000));
		savedKeyword.setCheckEveryTime(true);

		Keyword updatedKeyword = keywordRepository.save(savedKeyword);

		assertThat(updatedKeyword.getName()).isEqualTo("수정된 이체 키워드");
		assertThat(updatedKeyword.getDescription()).isEqualTo("수정된 이체 설명");
		assertThat(updatedKeyword.getAmount()).isEqualTo(BigDecimal.valueOf(100000));
		assertThat(updatedKeyword.getCheckEveryTime()).isTrue();
	}

	@Test
	@DisplayName("정산 키워드 수정 테스트")
	public void updateSettlementKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));
		Account testFromAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		Keyword keyword = new Keyword(testUser, KeywordType.SETTLEMENT, "원래 이름", "원래 설명", 100L, testFromAccount, "[]",
			null, false);
		Keyword savedKeyword = keywordRepository.save(keyword);

		savedKeyword.setName("수정된 정산 키워드");
		savedKeyword.setDescription("수정된 정산 설명");
		savedKeyword.setGroupMember("[{\"name\":\"김철수\",\"tel\":\"010-1234-5678\"}]");
		savedKeyword.setAmount(BigDecimal.valueOf(50000));
		savedKeyword.setCheckEveryTime(true);

		Keyword updatedKeyword = keywordRepository.save(savedKeyword);

		assertThat(updatedKeyword.getName()).isEqualTo("수정된 정산 키워드");
		assertThat(updatedKeyword.getDescription()).isEqualTo("수정된 정산 설명");
		assertThat(updatedKeyword.getGroupMember()).isEqualTo("[{\"name\":\"김철수\",\"tel\":\"010-1234-5678\"}]");
		assertThat(updatedKeyword.getAmount()).isEqualTo(BigDecimal.valueOf(50000));
		assertThat(updatedKeyword.getCheckEveryTime()).isTrue();
	}

	@Test
	@DisplayName("번호표 키워드 수정 테스트")
	public void updateTicketKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));

		Keyword keyword = new Keyword(testUser, KeywordType.TICKET, "원래 이름", "원래 설명", 100L, """
			{
				"address_name": "서울 성동구 성수동2가 289-10",
				"distance": "117",
				"id": "1841540654",
				"phone": "02-462-7627",
				"place_name": "하나은행 성수역지점",
				"road_address_name": "서울 성동구 성수이로 113",
				"x": "127.05717861008637",
				"y": "37.54512527783082"
			}
			""");
		Keyword savedKeyword = keywordRepository.save(keyword);

		savedKeyword.setName("수정된 티켓 키워드");
		savedKeyword.setDescription("수정된 티켓 설명");
		savedKeyword.setBranch("""
			{
				"address_name": "서울 성동구 성수동2가 289-10",
				"distance": "117",
				"id": "1841540654",
				"phone": "02-462-7627",
				"place_name": "하나은행 을지로2가지점",
				"road_address_name": "서울 성동구 성수이로 113",
				"x": "127.05717861008637",
				"y": "37.54512527783082"
			}
			""");

		Keyword updatedKeyword = keywordRepository.save(savedKeyword);

		assertThat(updatedKeyword.getName()).isEqualTo("수정된 티켓 키워드");
		assertThat(updatedKeyword.getDescription()).isEqualTo("수정된 티켓 설명");
		assertThat(updatedKeyword.getBranch()).isEqualTo("""
			{
				"address_name": "서울 성동구 성수동2가 289-10",
				"distance": "117",
				"id": "1841540654",
				"phone": "02-462-7627",
				"place_name": "하나은행 을지로2가지점",
				"road_address_name": "서울 성동구 성수이로 113",
				"x": "127.05717861008637",
				"y": "37.54512527783082"
			}
			""");
	}

	@Test
	@DisplayName("키워드 사용 테스트")
	public void useKeywordTest() throws Exception {
		User JunYongUser = userRepository.findFirstByUsername("JunYongID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));
		Keyword testKeyword = keywordRepository.findTopByUserIdAndType(JunYongUser.getId(),
			KeywordType.TRANSFER).orElseThrow(() -> new KeywordNotFoundException("Keyword not found"));

		assertThat(testKeyword).isNotNull();
		assertThat(testKeyword.getType()).isEqualTo(KeywordType.TRANSFER);
		assertThat(testKeyword.getName()).isEqualTo("성엽이 용돈");
		assertThat(testKeyword.getAmount()).isNotNull();
		assertThat(testKeyword.getSubAccount()).isNotNull();

	}

}
