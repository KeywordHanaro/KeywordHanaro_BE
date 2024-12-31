package com.hana4.keywordhanaro.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.repository.UserRepository;
import com.hana4.keywordhanaro.utils.JwtUtil;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	// @MockBean
	@Autowired
	private AuthenticationManager authenticationManager;

	// @MockBean
	@Autowired
	private JwtUtil jwtTokenProvider;

	@Test
	@DisplayName("로그인 성공 테스트")
	@WithMockUser(username = "admin")
	void loginSuccessTest(@Autowired UserRepository userRepository) throws Exception {
		UserDto loginRequest = new UserDto("admin", "password");
		String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("admin");
		// when(authenticationManager.authenticate(any())).thenReturn(authentication);
		// when(jwtTokenProvider.createJwt("admin")).thenReturn("test.jwt.token");

		ResultActions result = mockMvc.perform(post("/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(loginRequestJson)
		);

		result.andExpect(status().isOk())
			.andExpect(jsonPath("$.permission").value(
				userRepository.findFirstByUsername(authentication.getName()).get().getPermission()))
			.andDo(print());
	}


	@Test
	@DisplayName("로그인 실패 테스트 - 잘못된 자격 증명")
	void loginFailTest() throws Exception {
		UserDto loginRequest = new UserDto("admin", "wrongpassword");
		String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

		// when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginRequestJson))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.message").value("Incorrect username or password"))
			.andDo(print());
	}
}
