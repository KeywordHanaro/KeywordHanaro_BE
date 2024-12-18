package com.hana4.keywordhanaro.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;
import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.entity.user.UserStatus;
import com.hana4.keywordhanaro.repository.KeywordRepository;
import com.hana4.keywordhanaro.repository.TicketRepository;
import com.hana4.keywordhanaro.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TicketControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private KeywordRepository keywordRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@BeforeAll
	void beforeAll() {
		if (userRepository.findFirstByUsername("insunID").isEmpty()) {
			User testUser = new User("insunID", "insss123", "김인선", UserStatus.ACTIVE, 0);
			userRepository.save(testUser);
		}

		if (keywordRepository.findByName("inssTicketKeyword").isEmpty()) {
			User testUser = userRepository.findFirstByUsername("insunID").orElseThrow();
			String testBranch = """
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
				""";

			Keyword testKeyword = new Keyword(testUser, KeywordType.TICKET, 1L, "inssTicketKeyword", true, testBranch,
				"번호표 키워드 테스트 통과 ?!");
			keywordRepository.save(testKeyword);
		}
	}

	@AfterEach
	void tearDown() {
		ticketRepository.deleteAll();
	}

	@Test
	@DisplayName("번호표 기능 - 번호표 키워드 사용 시")
	void createTicketTestWithKeyword() throws Exception {
		// Given
		Keyword testKeyword = keywordRepository.findByName("inssTicketKeyword").orElseThrow();
		TicketRequestDto requestDto = TicketRequestDto.builder()
			.keywordId(testKeyword.getId())
			.workNumber((byte)1)
			.build();

		String requestBody = objectMapper.writeValueAsString(requestDto);

		mockMvc.perform(post("/ticket")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.user.id").value(testKeyword.getUser().getId()))
			.andExpect(jsonPath("$.branchId").isNotEmpty())
			.andExpect(jsonPath("$.branchName").isNotEmpty())
			.andExpect(jsonPath("$.waitingNumber").isNotEmpty())
			.andExpect(jsonPath("$.waitingGuest").isNotEmpty())
			.andExpect(jsonPath("$.workNumber").value(1))
			.andExpect(jsonPath("$.createAt").isNotEmpty());
	}

	@Test
	@DisplayName("번호표 기능 - 일반 번호표 사용 시")
	void createTicketTest() throws Exception {
		User testUser = userRepository.findFirstByUsername("insunID").orElseThrow();
		TicketRequestDto requestDto = TicketRequestDto.builder()
			.branchId(1841540654L)
			.branchName("하나은행 성수역지점")
			.userId(testUser.getId())
			.workNumber((byte)2)
			.build();

		System.out.println("requestDto = " + requestDto);

		String requestBody = objectMapper.writeValueAsString(requestDto);

		mockMvc.perform(post("/ticket")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.user.id").value(testUser.getId()))
			.andExpect(jsonPath("$.branchId").value(1841540654L))
			.andExpect(jsonPath("$.branchName").value("하나은행 성수역지점"))
			.andExpect(jsonPath("$.waitingNumber").isNotEmpty())
			.andExpect(jsonPath("$.waitingGuest").isNotEmpty())
			.andExpect(jsonPath("$.workNumber").value(2))
			.andExpect(jsonPath("$.createAt").isNotEmpty());
	}

}
