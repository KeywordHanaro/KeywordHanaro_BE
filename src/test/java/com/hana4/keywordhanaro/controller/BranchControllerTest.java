package com.hana4.keywordhanaro.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.BranchDto;
import com.hana4.keywordhanaro.service.BranchService;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(username = "insunID")
public class BranchControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BranchService branchService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("검색어로 영업점 검색 테스트")
	void getSearchBranchWithQuery() throws Exception {
		String query = "강남";
		Double lat = 37.5445598;
		Double lng = 127.0560563;
		List<BranchDto> list = Arrays.asList(
			new BranchDto("385462719",
				"하나은행 강남구청역지점",
				"서울 강남구 삼성동 9-1",
				"3512",
				"02-511-1111"),
			new BranchDto(
				"438040759",
				"하나은행 강남금융센터지점",
				"서울 강남구 삼성동 157-3",
				"4083",
				"02-3018-1111"));

		when(branchService.searchBranch(query, lat, lng)).thenReturn(list);
		String reqBody = objectMapper.writeValueAsString(list);

		mockMvc.perform(get("/branch/search")
				.param("query", query)
				.param("y", String.valueOf(lat))
				.param("x", String.valueOf(lng)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(reqBody));

	}

	@Test
	@DisplayName("검색어 없이 영업점 검색 테스트")
	void getSearchBranchWithoutQuery() throws Exception {
		// 현재 위치 성수
		Double lat = 37.5445598;
		Double lng = 127.0560563;
		List<BranchDto> list = Arrays.asList(
			new BranchDto("1733643823", "하나은행365 성수역", "서울 성동구 성수동2가 289-10", "114", ""),
			new BranchDto("1841540654", "하나은행 성수역지점", "서울 성동구 성수동2가 289-10", "117", "02-462-7627"));

		when(branchService.searchBranch(null, lat, lng)).thenReturn(list);
		String reqBody = objectMapper.writeValueAsString(list);

		mockMvc.perform(get("/branch/search")
				.param("y", String.valueOf(lat))
				.param("x", String.valueOf(lng)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(reqBody));

	}
}
