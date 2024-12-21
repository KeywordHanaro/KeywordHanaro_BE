package com.hana4.keywordhanaro.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.service.AccountService;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	AccountService accountService;

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
}
