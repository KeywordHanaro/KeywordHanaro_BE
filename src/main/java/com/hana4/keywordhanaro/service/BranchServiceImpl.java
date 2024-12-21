package com.hana4.keywordhanaro.service;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.hana4.keywordhanaro.model.dto.BranchResponseDto;
import com.hana4.keywordhanaro.model.mapper.BranchResponseMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
	@Value("${kakao.rest.api.key}")
	private String kakaoApiKey;
	@Value("${kakao.api.url}")
	private String kakaoApiUrl;

	private final WebClient webClient;

	@Override
	public Mono<List<BranchResponseDto>> searchBranches(String query, double lat, double lng, int radius) {
		URI uri = UriComponentsBuilder.fromUriString(kakaoApiUrl)
			.queryParam("query", "하나은행" + query)
			.queryParam("y", lat)
			.queryParam("x", lng)
			.queryParam("radius", radius)
			.queryParam("category_group_code", "BK9")
			.queryParam("sort", "distance")
			.encode()
			.build()
			.toUri();
		// System.out.println("uri = " + uri);

		return webClient.get()
			.uri(uri)
			.header("Authorization", "KakaoAK " + kakaoApiKey)
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
			})
			.map(response -> {
				List<Map<String, Object>> documents = (List<Map<String, Object>>)response.get("documents");

				if (documents == null) {
					documents = List.of();  // 없으면 빈 리스트
				}

				return documents.stream()
					.filter(branch -> branch.get("place_name").toString().contains("하나은행"))
					.map(BranchResponseMapper::toDto)
					.toList();

			});

	}

}
