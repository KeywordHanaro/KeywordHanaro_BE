package com.hana4.keywordhanaro.controller;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.BranchResponseDto;
import com.hana4.keywordhanaro.service.BranchService;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "insunID")
public class BranchControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private BranchService branchService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("검색어로 영업점 검색 테스트")
	void getSearchBranchWithQuery() throws Exception {
		String query = "강남";
		Double lat = 37.5445598;
		Double lng = 127.0560563;
		List<BranchResponseDto> list = Arrays.asList(
			new BranchResponseDto("385462719",
				"하나은행 강남구청역지점",
				"서울 강남구 삼성동 9-1",
				"3512",
				"02-511-1111"),
			new BranchResponseDto(
				"438040759",
				"하나은행 강남금융센터지점",
				"서울 강남구 삼성동 157-3",
				"4083",
				"02-3018-1111"));

		webTestClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/branch/search")
				.queryParam("query", query)
				.queryParam("y", String.valueOf(lat))
				.queryParam("x", String.valueOf(lng))
				.build())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$").isArray()
			.jsonPath("$[0].id").isEqualTo("385462719")
			.jsonPath("$[0].placeName").isEqualTo("하나은행 강남구청역지점")
			.jsonPath("$[1].id").isEqualTo("438040759")
			.jsonPath("$[1].placeName").isEqualTo("하나은행 강남금융센터지점");

	}

	@Test
	@DisplayName("검색어 없이 영업점 검색 테스트")
	void getSearchBranchWithoutQuery() throws Exception {
		// 현재 위치 성수
		Double lat = 37.5445598;
		Double lng = 127.0560563;
		List<BranchResponseDto> list = Arrays.asList(
			new BranchResponseDto("1733643823", "하나은행365 성수역", "서울 성동구 성수동2가 289-10", "114", ""),
			new BranchResponseDto("1841540654", "하나은행 성수역지점", "서울 성동구 성수동2가 289-10", "117", "02-462-7627"));

		webTestClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/branch/search")
				.queryParam("y", String.valueOf(lat))
				.queryParam("x", String.valueOf(lng))
				.build())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$").isArray()
			.jsonPath("$[0].id").isEqualTo("1733643823")
			.jsonPath("$[0].placeName").isEqualTo("하나은행365 성수역")
			.jsonPath("$[1].id").isEqualTo("1841540654")
			.jsonPath("$[1].placeName").isEqualTo("하나은행 성수역지점");

	}
}
