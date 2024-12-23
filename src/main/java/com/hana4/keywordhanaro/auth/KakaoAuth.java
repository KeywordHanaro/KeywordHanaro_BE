package com.hana4.keywordhanaro.auth;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoAuth {
	private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
	private static final String CLIENT_ID = "cf0b7e3e3feae9d12f5e11d619ffda3a";
	private static final String REDIRECT_URI = "http://localhost:3000/comps-test/step2";

	public static String getAccessToken(String authorizationCode) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String body = String.format(
			"grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s",
			CLIENT_ID, REDIRECT_URI, authorizationCode
		);

		HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
		ResponseEntity<Map> response = restTemplate.exchange(
			TOKEN_URL,
			HttpMethod.POST,
			requestEntity,
			Map.class
		);

		Map<String, Object> responseBody = response.getBody();
		return (String)responseBody.get("access_token");
	}
}
