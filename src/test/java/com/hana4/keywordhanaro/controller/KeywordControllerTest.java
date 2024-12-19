package com.hana4.keywordhanaro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.KeywordDto;
import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.account.AccountStatus;
import com.hana4.keywordhanaro.model.entity.account.AccountType;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.entity.user.UserStatus;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.BankRepository;
import com.hana4.keywordhanaro.repository.KeywordRepository;
import com.hana4.keywordhanaro.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
	void beforeAll() {
		if (userRepository.findFirstByUsername("insunID").isEmpty()) {
			User inssUser = new User("insunID", "insss123", "김인선", UserStatus.ACTIVE, 0);
			userRepository.save(inssUser);
		}

		if (accountRepository.findByAccountNumber("111-222-3342") == null) {
			User inssUser = userRepository.findFirstByUsername("insunID").orElseThrow();
			Bank bank = bankRepository.findAll().stream().findFirst().get();
			Account inssAccount = new Account("111-222-3342", inssUser, bank, "생활비 계좌", "1234", BigDecimal.valueOf(0),
				BigDecimal.valueOf(300000), AccountType.DEPOSIT,
				true, AccountStatus.ACTIVE);
			accountRepository.save(inssAccount);
		}

		if (accountRepository.findByAccountNumber("222-333-4455").isEmpty()) {
			User inssUser = userRepository.findFirstByUsername("insunID").orElseThrow();
			Bank bank = bankRepository.findAll().stream().findFirst().get();
			Account inssAccount = new Account("222-333-4455", inssUser, bank, "적금 계좌", "1234", BigDecimal.valueOf(0),
				BigDecimal.valueOf(300000), AccountType.SAVING,
				true, AccountStatus.ACTIVE);
			accountRepository.save(inssAccount);
		}
	}

	// @Test
	// @Order(1)
	// @DisplayName("조회 키워드 생성")
	// public void createInquiryKeywordTest() throws Exception {
	//
	// 	User testUser = userRepository.findFirstByUsername("insunID").orElseThrow();
	// 	Account testAccount = accountRepository.findByAccountNumber("111-222-3342").orElseThrow();
	//
	// 	KeywordDto keywordDto = KeywordDto.builder()
	// 		.user(testUser)
	// 		.type(KeywordType.INQUIRY.name())
	// 		.name("월급 조회")
	// 		.desc("생활비 계좌에서 조회 > 월급")
	// 		.inquiryWord("월급")
	// 		.accountId(testAccount)
	// 		.build();
	//
	// 	String requestBody = objectMapper.writeValueAsString(keywordDto);
	//
	// 	mockMvc.perform(post("/keywords")
	// 			.contentType(MediaType.APPLICATION_JSON)
	// 			.content(requestBody))
	// 		.andExpect(status().isOk())
	// 		.andExpect(jsonPath("$.type").value("INQUIRY"))
	// 		.andExpect(jsonPath("$.name").value("월급 조회"))
	// 		.andExpect(jsonPath("$.desc").value("생활비 계좌에서 조회 > 월급"))
	// 		.andExpect(jsonPath("$.accountId").value("111-222-3342"))
	// 		.andExpect(jsonPath("$.inquiryWord").value("월급"));
	// }

	// @Test
	// @Order(2)
	// @DisplayName("키워드 삭제 테스트")
	// void deleteKeywordTest() throws Exception {
	// 	Keyword keyword = keywordRepository.findAll().get(0);
	// 	mockMvc.perform(delete("/keywords/" + keyword.getId()))
	// 		.andExpect(status().isOk())
	// 		.andExpect(jsonPath("$.success").value("true"))
	// 		.andExpect(jsonPath("$.message").value("Keyword deleted successfully"));
	//
	// 	assertFalse(keywordRepository.existsById(keyword.getId()));
	// }


	@Test
	@Order(1)
	@DisplayName("조회(Inquiry) 키워드 수정 테스트")
	public void updateInquiryKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID").orElseThrow();
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342").orElseThrow(() ->new NullPointerException("Account not found"));

		Keyword originalKeyword = new Keyword(testUser, KeywordType.INQUIRY, "원래 이름", "원래 설명", 100L, testAccount, "원래 조회어");
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
	@Order(2)
	@DisplayName("이체(Transfer) 키워드 수정 테스트")
	public void updateTransferKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID").orElseThrow();
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342").orElseThrow(() ->new NullPointerException("Account not found"));
		Account subAccount = accountRepository.findByAccountNumber("222-333-4455").orElseThrow(() ->new NullPointerException("subAccount not found"));

		Keyword originalKeyword = new Keyword(testUser, KeywordType.TRANSFER, "원래 이름", "원래 설명", 100L, testAccount, subAccount, BigDecimal.valueOf(50000), false);
		originalKeyword = keywordRepository.save(originalKeyword);

		KeywordDto updateDto = KeywordDto.builder()
			.name("수정된 이체 키워드")
			.desc("수정된 이체 설명")
			.amount(BigDecimal.valueOf(100000))
			.checkEveryTime(true)
			.build();

		String requestBody = objectMapper.writeValueAsString(updateDto);

		mockMvc.perform(patch("/keyword/" + originalKeyword.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("수정된 이체 키워드"))
			.andExpect(jsonPath("$.desc").value("수정된 이체 설명"))
			.andExpect(jsonPath("$.amount").value(100000))
			.andExpect(jsonPath("$.checkEveryTime").value(true))
			.andExpect(jsonPath("$.type").value("TRANSFER"));
	}

	@Test
	@Order(3)
	@DisplayName("정산(Settlement) 키워드 수정 테스트")
	public void updateSettlementKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID").orElseThrow();
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342").orElseThrow(() ->new NullPointerException("Account not found"));

		Keyword originalKeyword = new Keyword(testUser, KeywordType.SETTLEMENT, "원래 이름", "원래 설명", 100L, testAccount, "[{\"name\":\"김철수\",\"tel\":\"010-1234-5678\"}]", BigDecimal.valueOf(50000), false);
		originalKeyword = keywordRepository.save(originalKeyword);

		KeywordDto updateDto = KeywordDto.builder()
			.name("수정된 정산 키워드")
			.desc("수정된 정산 설명")
			.groupMember("[{\"name\":\"김철수\",\"tel\":\"010-1234-5678\"},{\"name\":\"이영희\",\"tel\":\"010-8765-4321\"}]")
			.amount(BigDecimal.valueOf(100000))
			.checkEveryTime(true)
			.build();

		String requestBody = objectMapper.writeValueAsString(updateDto);

		mockMvc.perform(patch("/keyword/" + originalKeyword.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("수정된 정산 키워드"))
			.andExpect(jsonPath("$.desc").value("수정된 정산 설명"))
			.andExpect(jsonPath("$.groupMember").isNotEmpty())
			.andExpect(jsonPath("$.amount").value(100000))
			.andExpect(jsonPath("$.checkEveryTime").value(true))
			.andExpect(jsonPath("$.type").value("SETTLEMENT"));
	}

	@Test
	@Order(4)
	@DisplayName("티켓(Ticket) 키워드 수정 테스트")
	public void updateTicketKeywordTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID").orElseThrow();

		Keyword originalKeyword = new Keyword(testUser, KeywordType.TICKET, "원래 이름", "원래 설명", 100L, "{\"name\":\"하나은행 강남지점\",\"address\":\"서울특별시 강남구 테헤란로 152\",\"tel\":\"02-123-4567\"}");
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


}
