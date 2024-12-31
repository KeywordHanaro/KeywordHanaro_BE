package com.hana4.keywordhanaro.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.model.dto.AccountCheckDto;
import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.dto.AccountResponseDto;
import com.hana4.keywordhanaro.model.dto.DepositorCheckDto;
import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.service.AccountService;
import com.hana4.keywordhanaro.service.TransactionService;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	AccountService accountService;

	@MockBean
	TransactionService transactionService;

	@Autowired
	ObjectMapper objectMapper;

	final String url = "/account";

	@Test
	@DisplayName("[관리자] 단일 계좌 Test")
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	void getAccountTest() throws Exception {
		AccountDto account = new AccountDto(1L, "123-456-789", "testUser", new BigDecimal(1000));
		when(accountService.getAccount(account.getId())).thenReturn(account);
		String reqBody = objectMapper.writeValueAsString(account);
		mockMvc.perform(get(url + "/" + account.getId())).andExpect(status().isOk())
			.andExpect(content().json(reqBody));
	}

	@Test
	@DisplayName("[유저] 해당 유저 계좌 목록 조회")
	@WithMockUser(username = "user")
	void getMyAccountTest() throws Exception {
		List<AccountDto> mockAccounts = Arrays.asList(
			new AccountDto(1L, "123-456-789", "testUser", new BigDecimal(1000)),
			new AccountDto(2L, "987-654-321", "testUser", new BigDecimal(2000))
		);

		String reqBody = objectMapper.writeValueAsString(mockAccounts);

		when(accountService.getAccountsByUsername("user")).thenReturn(mockAccounts);

		mockMvc.perform(get(url + "/myaccounts")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(reqBody));
	}

	@Test
	@DisplayName("[관리자] 모든 계좌 조회")
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	void getAccountsTest() throws Exception {
		List<AccountDto> mockAccounts = Arrays.asList(
			new AccountDto(1L, "123-456-789", "testUser", new BigDecimal(1000)),
			new AccountDto(2L, "987-654-321", "testUser", new BigDecimal(2000))
		);

		String reqBody = objectMapper.writeValueAsString(mockAccounts);

		when(accountService.getAccounts()).thenReturn(mockAccounts);

		mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(reqBody));
	}

	@Test
	@DisplayName("[모두] 계좌 비밀번호 검증")
	@WithMockUser(username = "ues", roles = {"USER"})
	void checkPasswordTest() throws Exception {
		AccountDto accountDto = new AccountDto("1231-1231-1231", "1234");
		String reqBody = objectMapper.writeValueAsString(accountDto);
		System.out.println("reqBody = " + reqBody);

		when(accountService.checkPassword(accountDto.getAccountNumber(), accountDto.getPassword())).thenReturn(true);

		mockMvc.perform(post(url + "/checkPassword").contentType(MediaType.APPLICATION_JSON).content(reqBody))
			.andExpect(status().isOk())
			.andExpect(content().string("true"))
			.andDo(print());
	}

	@Test
	@DisplayName("[모두] 예금주 검증")
	@WithMockUser(username = "ues", roles = {"USER"})
	void checkAccountNumberAndBankTest() throws Exception {
		DepositorCheckDto depositorCheckDto = new DepositorCheckDto("1231-1231-1231", (short)1);
		String reqBody = objectMapper.writeValueAsString(depositorCheckDto);
		System.out.println("reqBody = " + reqBody);

		Map<String, Object> response = new HashMap<>();
		response.put("name", "남인우");
		when(accountService.checkAccountNumberAndBank(depositorCheckDto.getAccountNumber(), depositorCheckDto.getBankId())).thenReturn(response);

		mockMvc.perform(post(url + "/checkDepositor").contentType(MediaType.APPLICATION_JSON).content(reqBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("남인우"))
			.andDo(print());
	}

	@Test
	@DisplayName("[모두] 최근 거래 계좌 테스트")
	@WithMockUser(username = "kk")
	void getRecentTransactionsTest() throws Exception {
		List<AccountResponseDto> accountResponseDto = new ArrayList<>();
		when(transactionService.getRecentTransactionAccounts(1L)).thenReturn(accountResponseDto);

		mockMvc.perform(get(url + "/recentAccounts").param("accountId", String.valueOf(1L)))
			.andExpect(status().isOk())
			.andDo(print());
	}
}
