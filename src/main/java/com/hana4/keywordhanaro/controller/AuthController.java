package com.hana4.keywordhanaro.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.auth.JwtTokenProvider;
import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.dto.SettlementMultiReqDto;
import com.hana4.keywordhanaro.model.dto.SettlementReqDto;
import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.service.KakaoAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 전체 인증 흐름 처리
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "정산/회비 관련 API")
public class AuthController {

	private final KakaoAuthService kakaoAuthService;

	private final JwtTokenProvider jwtTokenProvider;

	@Operation(summary = "정산/회비 요청 메시지 전송", description = "정산 또는 회비 요청 시 계좌 번호, 금액 등을 포함한 메시지를 전송합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "정산/회비 요청 메시지 전송 성공"),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"server error message\" }")))})
	@PostMapping("/settlement/message")
	public ResponseEntity<Map<String, Object>> kakaoCallback(@RequestBody SettlementReqDto requestBody) {
		Map<String, Object> response = new HashMap<>();
		try {
			String code = requestBody.getCode();
			List<UserDto> groupMember = requestBody.getGroupMember();
			BigDecimal amount = requestBody.getAmount();
			AccountDto account = requestBody.getAccount();
			String type = requestBody.getType();

			String accessToken = kakaoAuthService.getAccessToken(code);

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

	@Operation(summary = "정산/회비 요청 다중 메시지 전송", description = "정산 또는 회비 요청 시 계좌 번호, 금액 등을 포함한 메시지를 전송합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "정산/회비 요청 메시지 전송 성공"),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"server error message\" }")))})
	@PostMapping("/settlement/message/multi")
	public ResponseEntity<Map<String, Object>> kakaoCallbackMulti(@RequestBody SettlementMultiReqDto requestBody) {
		Map<String, Object> response = new HashMap<>();
		try {
			String code = requestBody.getCode();
			List<SettlementReqDto> settlementList = requestBody.getSettlementList();

			String accessToken = kakaoAuthService.getAccessToken(code);

			for (short i = 0; i < settlementList.size(); i++) {
				kakaoAuthService.sendMessage(accessToken, settlementList.get(i).getGroupMember(),
					settlementList.get(i).getAmount(), settlementList.get(i).getAccount(),
					settlementList.get(i).getType());
			}

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
