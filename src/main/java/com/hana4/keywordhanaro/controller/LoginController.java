package com.hana4.keywordhanaro.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.exception.UnAuthorizedException;
import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.utils.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인 API")
public class LoginController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	@Operation(
		summary = "로그인",
		description = "사용자의 아이디와 비밀번호를 받아 인증 후 JWT 토큰을 반환합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인이 성공적으로 생성되었습니다."),
		@ApiResponse(responseCode = "401", description = "인증 실패",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Authentication failed\" }"
			))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"server error message\" }"
			)))
	})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto userDto) throws Exception {
		try {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
			);
		} catch (BadCredentialsException e) {
			throw new UnAuthorizedException("Incorrect username or password");
		}

		final UserDetails userDetails = userDetailsService
			.loadUserByUsername(userDto.getUsername());

		final String jwt = jwtUtil.createJwt(userDetails.getUsername());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwt);

		return ResponseEntity.ok().headers(headers).body("Login Successful");
	}
}
