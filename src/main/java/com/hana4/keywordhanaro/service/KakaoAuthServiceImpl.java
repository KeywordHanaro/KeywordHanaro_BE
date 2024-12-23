package com.hana4.keywordhanaro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 카카오 API와 통신하여 토큰과 사용자 정보 관리
 */
@Service
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements KakaoAuthService {
    private final RestTemplate restTemplate;

    @Value("${KAKAO_CLIENT_ID}")
    private String clientId;

    private final String redirectUri = "http://keyword.hanaro.topician.com";

    @Value("${KAKAO_CLIENT_SECRET}")
    private String clientSecret;
    @Value("${KAKAO_TOKEN_URL}")
    private String KAKAO_TOKEN_URL;
    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Override
    public String getAccessToken(String authorizationCode) {
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
            return (String) response.getBody().get("access_token");
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
}
