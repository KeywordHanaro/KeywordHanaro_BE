package com.hana4.keywordhanaro.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.auth.JwtTokenProvider;
import com.hana4.keywordhanaro.service.KakaoAuthService;

/**
 * 전체 인증 흐름 처리
 */
@SuppressWarnings("checkstyle:RegexpMultiline")
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
	@Autowired
	private KakaoAuthService kakaoAuthService;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@GetMapping("/kakao/callback")
	public ResponseEntity<Map<String, Object>> kakaoCallback(@RequestParam String code) {
		Map<String, Object> response = new HashMap<>();

		try {
			// 1. Access Token 요청
			String accessToken = kakaoAuthService.getAccessToken(code);
			// System.out.println("Access Token뭐야뭐야: " + accessToken);

			// 2. 사용자 정보 가져오기
			Map<String, Object> userInfo = kakaoAuthService.getUserInfo(accessToken);

			// 3. 사용자 등록 또는 검증 (생략 가능)
			String userId = userInfo.get("id").toString(); // 카카오 사용자 ID
			// 여기서 userId를 기반으로 DB에 등록하거나 기존 사용자 검증

			// 4. 서버 전용 JWT 발행
			String jwtToken = jwtTokenProvider.createToken(userId);

			// 5. 성공 응답 구성
			response.put("jwtToken", jwtToken);
			response.put("userInfo", userInfo);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// 에러 발생 시 JSON 응답 구성
			response.put("error", "Failed to process Kakao callback");
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}