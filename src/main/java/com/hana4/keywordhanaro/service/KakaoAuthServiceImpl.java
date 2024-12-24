package com.hana4.keywordhanaro.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.AccountDto;
import com.hana4.keywordhanaro.model.dto.UserDto;
import com.hana4.keywordhanaro.repository.UserRepository;

/**
 * 카카오 API와 통신하여 토큰과 사용자 정보 관리
 */
@Service
public class KakaoAuthServiceImpl implements KakaoAuthService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final UserRepository userRepository;

	@Value("${KAKAO_CLIENT_ID}")
	private String clientId;

	private final String redirectUri = "http://localhost:3000/settlement/kakao-login";

	@Value("${KAKAO_CLIENT_SECRET}")
	private String clientSecret;
	private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
	private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

	public KakaoAuthServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public String getAccessToken(String authorizationCode) {
		RestTemplate restTemplate = new RestTemplate();

		// HTTP 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// HTTP 바디 설정
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", clientId);
		body.add("redirect_uri", redirectUri);
		body.add("code", authorizationCode);
		body.add("client_secret", clientSecret);
		if (clientSecret != null && !clientSecret.isEmpty()) {
			body.add("client_secret", clientSecret);
		}

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

		// 요청 전송 및 응답 처리
		ResponseEntity<Map> response = restTemplate.exchange(
			KAKAO_TOKEN_URL,
			HttpMethod.POST,
			requestEntity,
			Map.class
		);

		// 응답 확인
		if (response.getStatusCode() == HttpStatus.OK) {
			System.out.println("Response Body: " + response.getBody());
			return (String)response.getBody().get("access_token");
		} else {
			throw new RuntimeException("Failed to get access token. Response: " + response.getBody());
		}
	}

	@Override
	public Map<String, Object> getUserInfo(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<Map> response = restTemplate.exchange(
			KAKAO_USER_INFO_URL,
			HttpMethod.GET,
			requestEntity,
			Map.class
		);

		return response.getBody();
	}

	@Override
	public void sendMessage(String accessToken, List<UserDto> groupMember, BigDecimal amount, AccountDto account,
		String type) {
		BigDecimal finalAmount;
		if (type.equals("Settlement")) {
			finalAmount = amount.divide(new BigDecimal(groupMember.size()), 0, RoundingMode.DOWN);
		} else {
			finalAmount = amount;
		}
		System.out.println(type);
		System.out.println(finalAmount);

		List<String> kakaoUUIDs = new ArrayList<>();

		for (UserDto user : groupMember) {
			kakaoUUIDs.add(userRepository.findKakaoUUIDByTel(user.getTel()));
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String uuidJson;
		try {
			uuidJson = objectMapper.writeValueAsString(kakaoUUIDs);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		// headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("receiver_uuids",
			"%s".formatted(uuidJson));
		// body.add("template_object",
		// 	"{\"object_type\":\"text\",\"text\":\"정산 요청: %s\",\"link\":{\"web_url\":\"http://localhost:3000\",\"mobile_web_url\":\"http://localhost:3000\"},\"button_title\":\"바로 확인\"}".formatted(
		// 		finalAmount));
		body.add("template_id", "115519");
		// body.add()

		// HttpEntity 생성 (헤더 + 바디)
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

		// System.out.println(request.getBody());
		String url = "https://kapi.kakao.com/v1/api/talk/friends/message/send";
		String response = restTemplate.exchange(url, HttpMethod.POST, request, String.class).getBody();

		// System.out.println(response);
	}
}
