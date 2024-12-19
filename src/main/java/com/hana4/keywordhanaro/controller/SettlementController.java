package com.hana4.keywordhanaro.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/settlement")
public class SettlementController {
    private static final String KAKAO_API_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private static final String KAKAO_ACCESS_TOKEN = "YOUR_ACCESS_TOKEN";

    @PostMapping
    public ResponseEntity<String> sendSettlementRequest(@RequestBody Map<String, Object> request) {
        List<String> users = (List<String>) request.get("users");
        String amount = (String) request.get("amount");

        for (String user : users) {
            sendKakaoMessage(user, amount);
        }

        return ResponseEntity.ok("정산 요청이 성공적으로 처리되었습니다.");
    }

    private void sendKakaoMessage(String userId, String amount) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + KAKAO_ACCESS_TOKEN);

        // 요청 데이터 설정
        String templateObject = String.format(
                """
                        {
                          "object_type": "text",
                          "text": "정산 요청: %s원을 정산해 주세요.",
                          "link": {
                            "web_url": "https://your-app-url.com",
                            "mobile_web_url": "https://your-app-url.com"
                          }
                        }
                        """, amount
        );

        Map<String, String> body = new HashMap<>();
        body.put("template_object", templateObject);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_API_URL, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            System.err.println("Failed to send message to user: " + userId);
        }
    }
}
