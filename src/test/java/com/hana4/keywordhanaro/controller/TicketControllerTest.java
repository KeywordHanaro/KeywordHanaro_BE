package com.hana4.keywordhanaro.controller;

import static org.assertj.core.api.Assertions.*;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.TicketDto;
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
@WithMockUser(username = "admin")
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
	@DisplayName("번호표 키워드 사용 시")
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
	@DisplayName("일반 번호표 사용 시")
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

	@Test
	@DisplayName("이미 발급받은 번호표가 있을 경우 기존 번호표 반환")
	void createTicketWithExistingTicket() throws Exception {
		// Given
		Keyword testKeyword = keywordRepository.findByName("inssTicketKeyword").orElseThrow();
		TicketRequestDto requestDto = TicketRequestDto.builder()
			.keywordId(testKeyword.getId())
			.workNumber((byte)1)
			.build();
		String requestBody = objectMapper.writeValueAsString(requestDto);

		// 첫번째 요청
		MvcResult firstResult = mockMvc.perform(post("/ticket")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andReturn();

		TicketDto firstTicket = objectMapper.readValue(
			firstResult.getResponse().getContentAsString(),
			TicketDto.class
		);

		// 두번때 요청
		MvcResult secondResult = mockMvc.perform(post("/ticket")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk())
			.andReturn();

		TicketDto secondTicket = objectMapper.readValue(
			secondResult.getResponse().getContentAsString(),
			TicketDto.class
		);

		assertThat(firstTicket.getId()).isEqualTo(secondTicket.getId());
		assertThat(firstTicket.getWaitingNumber()).isEqualTo(secondTicket.getWaitingNumber());
	}

	@Test
	@DisplayName("예외 처리 - 유효하지 않은 workNumber 입력")
	void testInvalidWorkNumber() throws Exception {
		TicketRequestDto requestDto = TicketRequestDto.builder()
			.keywordId(1L)
			.workNumber((byte)4)  // 유효하지 않은 업무 번호 (1~3만 가능)
			.build();

		String requestBody = objectMapper.writeValueAsString(requestDto);

		mockMvc.perform(post("/ticket")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Work number must be between 1 and 3"));
	}

	@Test
	@DisplayName("예외 처리 - 키워드 사용 시 필수 필드 누락")
	void testMissingRequiredFieldsForKeyword() throws Exception {
		TicketRequestDto requestDto = TicketRequestDto.builder()
			.keywordId(1L)
			// workNumber 누락
			.build();

		String requestBody = objectMapper.writeValueAsString(requestDto);

		mockMvc.perform(post("/ticket")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Work number is required for keyword ticket"));
	}

	@Test
	@DisplayName("예외 처리 - 일반 사용 시 필수 필드 누락")
	void testMissingRequiredFieldsForGeneral() throws Exception {
		TicketRequestDto requestDto = TicketRequestDto.builder()
			.workNumber((byte)1)
			// branchId 누락
			// branchName 누락
			.build();

		String requestBody = objectMapper.writeValueAsString(requestDto);

		mockMvc.perform(post("/ticket")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Branch ID is required"));
	}

	@Test
	@DisplayName("예외 처리 - 존재하지 않는 키워드로 요청")
	void testNonExistentKeyword() throws Exception {
		TicketRequestDto requestDto = TicketRequestDto.builder()
			.keywordId(999L)
			.workNumber((byte)1)
			.build();

		String requestBody = objectMapper.writeValueAsString(requestDto);

		mockMvc.perform(post("/ticket")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("Keyword not found"));
	}

}
