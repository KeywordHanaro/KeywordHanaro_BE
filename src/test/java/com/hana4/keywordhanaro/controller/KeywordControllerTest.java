package com.hana4.keywordhanaro.controller;

import static org.hamcrest.collection.IsCollectionWithSize.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.exception.KeywordNotFoundException;
import com.hana4.keywordhanaro.exception.UserNotFoundException;
import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.dto.MultiKeywordDto;
import com.hana4.keywordhanaro.model.dto.SubKeywordDto;
import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.entity.user.UserStatus;
import com.hana4.keywordhanaro.model.mapper.AccountResponseMapper;
import com.hana4.keywordhanaro.model.mapper.UserResponseMapper;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.BankRepository;
import com.hana4.keywordhanaro.repository.KeywordRepository;
import com.hana4.keywordhanaro.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "insunID")
public class KeywordControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private KeywordRepository keywordRepository;

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

		if (userRepository.findFirstByUsername("yeobID").isEmpty()) {
			User yeobUser = new User("yeobID", "yeobbbb", "정성엽", UserStatus.ACTIVE, 0);
			userRepository.save(yeobUser);
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

		if (accountRepository.findByAccountNumber("222-342-2223").isEmpty()) {
			User yeobUser = userRepository.findFirstByUsername("yeobID")
				.orElseThrow(() -> new UserNotFoundException("User not found"));
			Bank bank = bankRepository.findAll().stream().findFirst().get();
			Account yeobAccount = new Account("222-342-2223", yeobUser, bank, "성엽이 계좌", "1234", BigDecimal.valueOf(0),
				BigDecimal.valueOf(400000), AccountType.DEPOSIT,
				AccountStatus.ACTIVE);
			accountRepository.save(yeobAccount);
		}

		User yeobUser = userRepository.findFirstByUsername("yeobID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));

		List<KeywordType> existingTypes = keywordRepository.findTypesByUserId(yeobUser.getId());
		// List<KeywordType> missingTypes = new ArrayList<>(Arrays.asList(KeywordType.values()));
		// missingTypes.removeAll(existingTypes);

		if (existingTypes.size() < 4) {
			Account inssAccount = accountRepository.findByAccountNumber("111-222-3342")
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));
			Account yeobAccount = accountRepository.findByAccountNumber("222-342-2223")
				.orElseThrow(() -> new AccountNotFoundException("SubAccount not found"));

			Keyword k1 = new Keyword(yeobUser, KeywordType.INQUIRY, "밥값 조회", "조회 사용 테스트", 100L, inssAccount, "밥값");
			Keyword k2 = new Keyword(yeobUser, KeywordType.TRANSFER, "성엽이 용돈", "송금 사용 테스트", 200L, inssAccount,
				yeobAccount, BigDecimal.valueOf(50000), false);
			Keyword k3 = new Keyword(yeobUser, KeywordType.SETTLEMENT, "터틀넥즈 정산", "정산 사용 테스트", 300L, inssAccount,
				"[{\"name\":\"김도희\",\"tel\":\"010-1234-5678\"},{\"name\":\"문서아\",\"tel\":\"010-8765-4321\"}]",
				BigDecimal.valueOf(20000),
				false);
			Keyword k4 = new Keyword(yeobUser, KeywordType.TICKET, "성수역점 번호표", "번호표 사용 테스트", 400L,
				"{\"place_name\":\"하나은행 성수역지점\",\"address_name\":\"서울 성동구 성수동2가 289-10\",\"phone\":\"02-462-7627\",\"distance\":\"117\",\"id\":\"1841540654\"}");

			keywordRepository.saveAll(Arrays.asList(k1, k2, k3, k4));

		}

	}

	@Test
	@DisplayName("조회 키워드 생성 테스트")
	public void createInquiryKeywordTest() throws Exception {

		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		KeywordDto keywordDto = KeywordDto.builder()
			.user(UserResponseMapper.toDto(testUser))
			.type(KeywordType.INQUIRY.name())
			.name("월급 조회")
			.desc("생활비 계좌에서 조회 > 월급")
			.inquiryWord("월급")
			.account(AccountResponseMapper.toDto(testAccount))
			.build();

		String requestBody = objectMapper.writeValueAsString(keywordDto);

		mockMvc.perform(post("/keyword")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.type").value("INQUIRY"))
			.andExpect(jsonPath("$.name").value(keywordDto.getName()))
			.andExpect(jsonPath("$.desc").value(keywordDto.getDesc()))
			.andExpect(jsonPath("$.account.id").value(testAccount.getId()))
			.andExpect(jsonPath("$.seqOrder", Matchers.greaterThan(0)))
			.andExpect(jsonPath("$.inquiryWord").value(keywordDto.getInquiryWord()));
	}

	@Test
	@DisplayName("송금 키워드 생성 테스트")
	public void createTransferKeywordTest() throws Exception {

		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		Account testSubAccount = accountRepository.findByAccountNumber("222-342-2223")
			.orElseThrow(() -> new AccountNotFoundException("Receiving account not found"));

		KeywordDto keywordDto = KeywordDto.builder()
			.user(UserResponseMapper.toDto(testUser))
			.type(KeywordType.TRANSFER.name())
			.name("성엽이 용돈")
			.desc("생활비계좌에서 > 성엽이계좌 > 5만원")
			.account(AccountResponseMapper.toDto(testAccount))
			.subAccount(AccountResponseMapper.toDto(testSubAccount))
			.amount(BigDecimal.valueOf(50000))
			.checkEveryTime(false)
			.build();

		String requestBody = objectMapper.writeValueAsString(keywordDto);

		mockMvc.perform(post("/keyword")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.type").value("TRANSFER"))
			.andExpect(jsonPath("$.name").value(keywordDto.getName()))
			.andExpect(jsonPath("$.desc").value(keywordDto.getDesc()))
			.andExpect(jsonPath("$.account.id").value(testAccount.getId()))
			.andExpect(jsonPath("$.subAccount.id").value(testSubAccount.getId()))
			.andExpect(jsonPath("$.seqOrder", Matchers.greaterThan(0)));
	}

	@Test
	@DisplayName("번호표 키워드 생성 테스트")
	public void createTicketKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));
		String testBranch = "{\"address_name\": \"서울 성동구 성수동2가 289-10\", \"distance\": \"117\", \"id\": \"1841540654\", \"phone\": \"02-462-7627\", \"place_name\": \"하나은행 성수역지점\"}";
		System.out.println("testBranch = " + testBranch);

		KeywordDto keywordDto = KeywordDto.builder()
			.user(UserResponseMapper.toDto(testUser))
			.type(KeywordType.TICKET.name())
			.name("성수역점 번호표")
			.desc("번호표 > 성수역점")
			.branch(testBranch)
			.build();

		String requestBody = objectMapper.writeValueAsString(keywordDto);
		System.out.println("requestBody = " + requestBody);

		mockMvc.perform(post("/keyword")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.type").value("TICKET"))
			.andExpect(jsonPath("$.name").value(keywordDto.getName()))
			.andExpect(jsonPath("$.desc").value(keywordDto.getDesc()))
			.andExpect(jsonPath("$.seqOrder", Matchers.greaterThan(0)))
			.andExpect(jsonPath("$.branch").value(testBranch));
	}

	@Test
	@DisplayName("정산 키워드 생성 테스트")
	public void createSettlementKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		String testGroupMember = """
			[
			    {
			        "name": "김도희",
			        "tel": "010-4622-7627"
			    },
			    {
			        "name": "문서아",
			        "tel": "0190-1223-4567"
			    }
			]
			""";

		KeywordDto keywordDto = KeywordDto.builder()
			.user(UserResponseMapper.toDto(testUser))
			.type(KeywordType.SETTLEMENT.name())
			.name("러닝크루 정산")
			.desc("정산 > 김도희, 문서아")
			.account(AccountResponseMapper.toDto(testAccount))
			.groupMember(testGroupMember)
			.checkEveryTime(true)
			.build();

		String requestBody = objectMapper.writeValueAsString(keywordDto);

		mockMvc.perform(post("/keyword")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.type").value("SETTLEMENT"))
			.andExpect(jsonPath("$.name").value(keywordDto.getName()))
			.andExpect(jsonPath("$.desc").value(keywordDto.getDesc()))
			.andExpect(jsonPath("$.seqOrder", Matchers.greaterThan(0)))
			.andExpect(jsonPath("$.account.id").value(testAccount.getId()))
			.andExpect(jsonPath("$.groupMember").value(testGroupMember));

	}

	@Test
	@DisplayName("조회(Inquiry) 키워드 수정 테스트")
	public void updateInquiryKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		Keyword originalKeyword = new Keyword(testUser, KeywordType.INQUIRY, "원래 이름", "원래 설명", 100L, testAccount,
			"원래 조회어");
		originalKeyword = keywordRepository.save(originalKeyword);

		KeywordDto updateDto = KeywordDto.builder()
			.name("수정된 조회 키워드")
			.desc("수정된 조회 설명")
			.inquiryWord("수정된 조회어")
			// .isFavorite(false)
			.build();

		String requestBody = objectMapper.writeValueAsString(updateDto);

		mockMvc.perform(patch("/keyword/" + originalKeyword.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("수정된 조회 키워드"))
			.andExpect(jsonPath("$.desc").value("수정된 조회 설명"))
			.andExpect(jsonPath("$.inquiryWord").value("수정된 조회어"))
			.andExpect(jsonPath("$.type").value("INQUIRY"));
	}

	@Test
	@DisplayName("이체(Transfer) 키워드 수정 테스트")
	public void updateTransferKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		Account subAccount = accountRepository.findByAccountNumber("222-333-4455")
			.orElseThrow(() -> new AccountNotFoundException("subAccount not found"));

		Keyword originalKeyword = new Keyword(testUser, KeywordType.TRANSFER, "원래 이름", "원래 설명", 100L, testAccount,
			subAccount, BigDecimal.valueOf(50000), false);
		originalKeyword = keywordRepository.save(originalKeyword);

		KeywordDto updateDto = KeywordDto.builder()
			.name("수정된 이체 키워드")
			.desc("수정된 이체 설명")
			.amount(BigDecimal.valueOf(100000))
			.checkEveryTime(false)
			.build();

		String requestBody = objectMapper.writeValueAsString(updateDto);

		mockMvc.perform(patch("/keyword/" + originalKeyword.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("수정된 이체 키워드"))
			.andExpect(jsonPath("$.desc").value("수정된 이체 설명"))
			.andExpect(jsonPath("$.amount").value(100000))
			.andExpect(jsonPath("$.checkEveryTime").value(false))
			.andExpect(jsonPath("$.type").value("TRANSFER"));
	}

	@Test
	@DisplayName("정산(Settlement) 키워드 수정 테스트")
	public void updateSettlementKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		Keyword originalKeyword = new Keyword(testUser, KeywordType.SETTLEMENT, "원래 이름", "원래 설명", 100L, testAccount,
			"[{\"name\":\"김철수\",\"tel\":\"010-1234-5678\"}]", BigDecimal.valueOf(50000), false);
		originalKeyword = keywordRepository.save(originalKeyword);

		KeywordDto updateDto = KeywordDto.builder()
			.name("수정된 정산 키워드")
			.desc("수정된 정산 설명")
			.groupMember("[{\"name\":\"김철수\",\"tel\":\"010-1234-5678\"},{\"name\":\"이영희\",\"tel\":\"010-8765-4321\"}]")
			.amount(BigDecimal.valueOf(100000))
			.checkEveryTime(false)
			.build();

		String requestBody = objectMapper.writeValueAsString(updateDto);

		mockMvc.perform(patch("/keyword/" + originalKeyword.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(jsonPath("$.name").value("수정된 정산 키워드"))
			.andExpect(jsonPath("$.desc").value("수정된 정산 설명"))
			.andExpect(jsonPath("$.groupMember").isNotEmpty())
			.andExpect(jsonPath("$.amount").value(100000))
			.andExpect(jsonPath("$.checkEveryTime").value(false))
			.andExpect(jsonPath("$.type").value("SETTLEMENT"));
	}

	@Test
	@DisplayName("티켓(Ticket) 키워드 수정 테스트")
	public void updateTicketKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));

		Keyword originalKeyword = new Keyword(testUser, KeywordType.TICKET, "원래 이름", "원래 설명", 100L,
			"{\"name\":\"하나은행 강남지점\",\"address\":\"서울특별시 강남구 테헤란로 152\",\"tel\":\"02-123-4567\"}");
		originalKeyword = keywordRepository.save(originalKeyword);
		KeywordDto updateDto = KeywordDto.builder()
			.name("수정된 티켓 키워드")
			.desc("수정된 티켓 설명")
			.branch("{\"name\":\"하나은행 서초지점\",\"address\":\"서울특별시 서초구 서초대로 330\",\"tel\":\"02-987-6543\"}")
			.build();

		String requestBody = objectMapper.writeValueAsString(updateDto);

		mockMvc.perform(patch("/keyword/" + originalKeyword.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("수정된 티켓 키워드"))
			.andExpect(jsonPath("$.desc").value("수정된 티켓 설명"))
			.andExpect(jsonPath("$.branch").isNotEmpty())
			.andExpect(jsonPath("$.type").value("TICKET"));
	}

	@Test
	@DisplayName("키워드 삭제 테스트")
	void deleteKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		Keyword originalKeyword = new Keyword(testUser, KeywordType.INQUIRY, "원래 이름", "원래 설명", 100L, testAccount,
			"원래 조회어");
		originalKeyword = keywordRepository.save(originalKeyword);

		Keyword keyword = keywordRepository.findAll().get(0);
		mockMvc.perform(delete("/keyword/" + keyword.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value("true"))
			.andExpect(jsonPath("$.message").value("Keyword deleted successfully"));

		assertFalse(keywordRepository.existsById(keyword.getId()));
	}

	@Test
	@DisplayName("예외처리 - 잘못된 키워드 타입으로 요청")
	public void createInvalidTypeKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));
		KeywordDto keywordDto = KeywordDto.builder()
			.user(UserResponseMapper.toDto(testUser))
			.type("INVALID_TYPE")
			.name("잘못된 타입")
			.desc("잘못된 타입의 키워드")
			.build();

		String requestBody = objectMapper.writeValueAsString(keywordDto);

		mockMvc.perform(post("/keyword")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Invalid keyword type"));
	}

	@Test
	@DisplayName("예외처리 - 필수 필드 누락")
	public void createKeywordWithMissingFieldTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));
		KeywordDto keywordDto = KeywordDto.builder()
			.user(UserResponseMapper.toDto(testUser))
			.type(KeywordType.INQUIRY.name())
			// name 필드 누락
			.desc("필수 필드 누락 테스트")
			.build();

		String requestBody = objectMapper.writeValueAsString(keywordDto);

		mockMvc.perform(post("/keyword")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Keyword name is required"));
	}

	@Test
	@DisplayName("예외처리 - checkEveryTime이 true일 때 amount 포함")
	public void createTransferKeywordWithInvalidAmountTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID")
			.orElseThrow(() -> new UserNotFoundException("User not found!!"));
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		Account testSubAccount = accountRepository.findByAccountNumber("222-342-2223")
			.orElseThrow(() -> new AccountNotFoundException("SubAccount not found"));

		KeywordDto keywordDto = KeywordDto.builder()
			.user(UserResponseMapper.toDto(testUser))
			.type(KeywordType.TRANSFER.name())
			.name("잘못된 송금 키워드")
			.desc("checkEveryTime이 true인데 amount가 있음")
			.account(AccountResponseMapper.toDto(testAccount))
			.subAccount(AccountResponseMapper.toDto(testSubAccount))
			.amount(BigDecimal.valueOf(50000))
			.checkEveryTime(true)
			.build();

		String requestBody = objectMapper.writeValueAsString(keywordDto);

		mockMvc.perform(post("/keyword")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Amount should not be provided when checkEveryTime is true"));
	}

	@Test
	@DisplayName("조회 키워드 사용 테스트")
	public void useInquiryKeywordTest() throws Exception {
		User yeobUser = userRepository.findFirstByUsername("yeobID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));
		Keyword inquiryKeyword = keywordRepository.findTopByUserIdAndType(yeobUser.getId(),
			KeywordType.INQUIRY).orElseThrow(() -> new KeywordNotFoundException("Keyword not found"));

		mockMvc.perform(get("/keyword/" + inquiryKeyword.getId()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.type").value("INQUIRY"))
			.andExpect(jsonPath("$.inquiryWord").value(inquiryKeyword.getInquiryWord()))
			.andExpect(jsonPath("$.account.id").value(inquiryKeyword.getAccount().getId()))
			.andExpect(jsonPath("$.transactions").isArray())
			.andDo(print());
	}

	@Test
	@DisplayName("송금 키워드 사용 테스트")
	public void useTransferKeywordTest() throws Exception {
		User yeobUser = userRepository.findFirstByUsername("yeobID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));
		Keyword transferKeyword = keywordRepository.findTopByUserIdAndType(yeobUser.getId(),
			KeywordType.TRANSFER).orElseThrow(() -> new KeywordNotFoundException("Keyword not found"));

		mockMvc.perform(get("/keyword/" + transferKeyword.getId()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.type").value("TRANSFER"))
			.andExpect(jsonPath("$.account.id").value(transferKeyword.getAccount().getId()))
			.andExpect(jsonPath("$.subAccount.id").value(transferKeyword.getSubAccount().getId()))
			.andDo(print());
	}

	@Test
	@DisplayName("번호표 키워드 사용 테스트")
	public void useTicketKeywordTest() throws Exception {
		User yeobUser = userRepository.findFirstByUsername("yeobID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));
		Keyword ticketKeyword = keywordRepository.findTopByUserIdAndType(yeobUser.getId(),
			KeywordType.TICKET).orElseThrow(() -> new KeywordNotFoundException("Keyword not found"));

		mockMvc.perform(get("/keyword/" + ticketKeyword.getId()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.type").value("TICKET"))
			.andExpect(jsonPath("$.branch").isNotEmpty())
			.andDo(print());
	}

	@Test
	@DisplayName("정산 키워드 사용 테스트")
	public void useSettlementKeywordTest() throws Exception {
		User yeobUser = userRepository.findFirstByUsername("yeobID")
			.orElseThrow(() -> new UserNotFoundException("User not found"));
		Keyword settlementKeyword = keywordRepository.findTopByUserIdAndType(yeobUser.getId(),
			KeywordType.SETTLEMENT).orElseThrow(() -> new KeywordNotFoundException("Keyword not found"));

		mockMvc.perform(get("/keyword/" + settlementKeyword.getId()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.type").value("SETTLEMENT"))
			.andExpect(jsonPath("$.groupMember").isNotEmpty())
			.andExpect(jsonPath("$.account.id").value(settlementKeyword.getAccount().getId()))
			.andDo(print());
	}

	@Test
	@DisplayName("내 모든 키워드 조회 테스트")
	@WithMockUser(username = "yeobID")
	void getKeywordsTest() throws Exception {
		mockMvc.perform(get("/keyword"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(4)))
			.andDo(print());
	}

	@Test
	@DisplayName("멀티 키워드 생성 테스트")
	@Transactional
	@Rollback(false)
	public void createMultiKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID").orElseThrow();
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342")
			.orElseThrow(() -> new IllegalStateException("Account not found"));
		Account testSubAccount = accountRepository.findByAccountNumber("222-342-2223")
			.orElseThrow(() -> new IllegalStateException("Sub-account not found"));

		// 키워드 먼저 우선 저장
		Keyword inquiryKeyword = Keyword.builder()
			.type(KeywordType.INQUIRY)
			.name("잔액 조회")
			.description("잔액 조회하기")
			.account(testAccount)
			.inquiryWord("잔액")
			.user(testUser)
			.seqOrder(3L)
			.build();
		inquiryKeyword = keywordRepository.save(inquiryKeyword);

		Keyword transferKeyword = Keyword.builder()
			.type(KeywordType.TRANSFER)
			.name("성엽 용돈")
			.description("생활비계좌에서 > 성엽계좌 > 3만원")
			.account(testAccount)
			.subAccount(testSubAccount)
			.amount(BigDecimal.valueOf(30000))
			.checkEveryTime(false)
			.user(testUser)
			.seqOrder(5L)
			.build();
		transferKeyword = keywordRepository.save(transferKeyword);

		System.out.println(
			"Pre-created keywords: inquiry = " + inquiryKeyword.getId() + ", transfer = " + transferKeyword.getId());

		List<MultiKeywordDto> multiKeywords = List.of(
			MultiKeywordDto.builder()
				.keyword(SubKeywordDto.builder()
					.id(inquiryKeyword.getId())
					.build())
				.seqOrder((byte)1)
				.build(),
			MultiKeywordDto.builder()
				.keyword(SubKeywordDto.builder()
					.id(transferKeyword.getId())
					.build())
				.seqOrder((byte)2)
				.build()
		);

		KeywordDto keywordDto = KeywordDto.builder()
			.user(UserResponseMapper.toDto(testUser))
			.type(KeywordType.MULTI.name())
			.name("멀티 키워드")
			.desc("잔액 조회 후 용돈 보내기")
			.multiKeyword(multiKeywords)
			.build();

		String requestBody = objectMapper.writeValueAsString(keywordDto);

		System.out.println("requestBody = " + requestBody);

		mockMvc.perform(post("/keyword")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.type").value("MULTI"))
			.andExpect(jsonPath("$.name").value(keywordDto.getName()))
			.andExpect(jsonPath("$.desc").value(keywordDto.getDesc()))
			.andExpect(jsonPath("$.multiKeyword", Matchers.hasSize(2)))
			.andExpect(jsonPath("$.multiKeyword[0].keyword.id").value(inquiryKeyword.getId()))
			.andExpect(jsonPath("$.multiKeyword[1].keyword.id").value(transferKeyword.getId()));
		;
	}

}
