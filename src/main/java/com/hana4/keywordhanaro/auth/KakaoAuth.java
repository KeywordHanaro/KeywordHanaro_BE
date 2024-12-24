package com.hana4.keywordhanaro.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Getter
@Setter
public class KakaoAuth {

    @Value("${KAKAO_TOKEN_URL}")
    private static String TOKEN_URL;
    @Value("${KAKAO_CLIENT_ID}")
    private static String CLIENT_ID;
    // todo 정산메세지 완료 후 uri확정예정
    private static final String REDIRECT_URI = "http://keyword.hanaro.topician.com";

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
        return (String) responseBody.get("access_token");
    }
}
