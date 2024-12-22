package com.hana4.keywordhanaro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KakaoConfig {
	@Value("${kakao.api.url}")
	private String kakaoApiUrl;

	// webClient는 수동으로 빈 등록 필요
	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder.baseUrl(kakaoApiUrl).build();
	}
}
