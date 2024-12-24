package com.hana4.keywordhanaro.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.auth.JwtTokenProvider;
import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.dto.SettlementReqDto;
import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.service.KakaoAuthService;

/**
 * 전체 인증 흐름 처리
 */
@SuppressWarnings("checkstyle:RegexpMultiline")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
	@Autowired
	private KakaoAuthService kakaoAuthService;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@PostMapping("/settlement/message")
	public ResponseEntity<Map<String, Object>> kakaoCallback(@RequestBody SettlementReqDto requestBody) {
		Map<String, Object> response = new HashMap<>();
		try {
			String code = requestBody.getCode();
			List<UserDto> groupMember = requestBody.getGroupMember();
			BigDecimal amount = requestBody.getAmount();
			AccountDto account = requestBody.getAccount();
			String type = requestBody.getType();
			// for (UserDto user : groupMember) {
			// 	System.out.println(user.getName());
			// }

			String accessToken = kakaoAuthService.getAccessToken(code);
			// System.out.println(accessToken);

			kakaoAuthService.sendMessage(accessToken, groupMember, amount, account, type);

			response.put("success", "success");

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// 에러 발생 시 JSON 응답 구성
			response.put("error", "Failed to process Kakao callback");
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}

