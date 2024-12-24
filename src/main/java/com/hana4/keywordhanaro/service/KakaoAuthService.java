package com.hana4.keywordhanaro.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.dto.UserDto;

public interface KakaoAuthService {
	/**
	 * 카카오 API로부터 Access Token을 요청.
	 *
	 * @param authorizationCode 카카오 인증 서버에서 반환된 인가 코드
	 * @return Access Token
	 */
	String getAccessToken(String authorizationCode);

	/**
	 * Access Token을 사용하여 카카오 사용자 정보를 조회.
	 *
	 * @param accessToken 카카오 API에서 발급받은 Access Token
	 * @return 사용자 정보 (JSON 형태)
	 */
	Map<String, Object> getUserInfo(String accessToken);

	void sendMessage(String accessToken, List<UserDto> groupMember, BigDecimal amount, AccountDto account,
		String type);
}
