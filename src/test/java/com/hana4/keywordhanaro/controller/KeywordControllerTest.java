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
	}

	@Test
	@Order(1)
	@DisplayName("조회 키워드 생성")
	public void createInquiryKeywordTest() throws Exception {

		User testUser = userRepository.findFirstByUsername("insunID").orElseThrow();
		Account testAccount = accountRepository.findByAccountNumber("111-222-3342");

		KeywordDto keywordDto = KeywordDto.builder()
			.user(testUser)
			.type(KeywordType.INQUIRY.name())
			.name("월급 조회")
			.desc("생활비 계좌에서 조회 > 월급")
			.inquiryWord("월급")
			.accountId(testAccount)
			.build();

		String requestBody = objectMapper.writeValueAsString(keywordDto);

		mockMvc.perform(post("/keywords")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.type").value("INQUIRY"))
			.andExpect(jsonPath("$.name").value("월급 조회"))
			.andExpect(jsonPath("$.desc").value("생활비 계좌에서 조회 > 월급"))
			.andExpect(jsonPath("$.accountId").value("111-222-3342"))
			.andExpect(jsonPath("$.inquiryWord").value("월급"));
	}

	@Test
	@Order(2)
	@DisplayName("키워드 삭제 테스트")
	void deleteKeywordTest() throws Exception {
		Keyword keyword = keywordRepository.findAll().get(0);
		mockMvc.perform(delete("/keywords/" + keyword.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value("true"))
			.andExpect(jsonPath("$.message").value("Keyword deleted successfully"));

		assertFalse(keywordRepository.existsById(keyword.getId()));
	}
	// Add similar tests for other types...
}

