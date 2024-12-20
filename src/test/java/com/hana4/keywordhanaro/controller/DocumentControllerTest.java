package com.hana4.keywordhanaro.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.service.DocumentService;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "kkk")
class DocumentControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	DocumentService documentService;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ObjectMapper objectMapper;

	final String url = "/document";

	@Test
	void addTransferDocumentTest() throws Exception {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		Account account = accountRepository.findAll().get(0);
		Account account2 = accountRepository.findAll().get(1);
		requestMap.put("account", account);
		requestMap.put("subAccount", account2);
		requestMap.put("amount", "10000");

		String reqBody = objectMapper.writeValueAsString(requestMap);

		System.out.println("reqBody = " + reqBody);

		mockMvc.perform(MockMvcRequestBuilders.post(url + "/transfer")
				.contentType(MediaType.APPLICATION_JSON).content(reqBody))
			.andExpect(status().isOk())
			.andExpect(content().contentType("text/plain;charset=UTF-8"))
			.andExpect(content().string("transfer document completed"))
			.andDo(print());
	}

	@Test
	void addWithDrawDocumentTest() throws Exception {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		Account account = accountRepository.findAll().get(0);
		requestMap.put("account", account);
		requestMap.put("amount", "10000");

		String reqBody = objectMapper.writeValueAsString(requestMap);

		System.out.println("reqBody = " + reqBody);

		mockMvc.perform(MockMvcRequestBuilders.post(url + "/withdraw")
				.contentType(MediaType.APPLICATION_JSON).content(reqBody))
			.andExpect(status().isOk())
			.andExpect(content().contentType("text/plain;charset=UTF-8"))
			.andExpect(content().string("withdraw document completed"))
			.andDo(print());
	}

	@Test
	void addDepositDocumentTest() throws Exception {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		Account account = accountRepository.findAll().get(0);
		requestMap.put("subAccount", account);
		requestMap.put("amount", "10000");

		String reqBody = objectMapper.writeValueAsString(requestMap);

		System.out.println("reqBody = " + reqBody);

		mockMvc.perform(MockMvcRequestBuilders.post(url + "/deposit")
				.contentType(MediaType.APPLICATION_JSON).content(reqBody))
			.andExpect(status().isOk())
			.andExpect(content().contentType("text/plain;charset=UTF-8"))
			.andExpect(content().string("deposit document completed"))
			.andDo(print());
	}
}
