package com.hana4.keywordhanaro.service;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.hana4.keywordhanaro.exception.ApiRequestException;
import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.model.dto.BranchResponseDto;
import com.hana4.keywordhanaro.model.mapper.BranchResponseMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
	@Value("${KAKAO_CLIENT_ID}")
	private String kakaoApiKey;
	@Value("${kakao.api.url}")
	private String kakaoApiUrl;

	private final WebClient webClient;

	@Override
	public Mono<List<BranchResponseDto>> searchBranch(String query, Double lat, Double lng) {
		validateSearchParams(lat, lng);

		if (query == null || query.trim().isEmpty()) {
			return searchNearbyBranches(lat, lng);
		} else {
			return searchBranchesByQuery(query, lat, lng);
		}

	}

	private Mono<List<BranchResponseDto>> searchNearbyBranches(Double lat, Double lng) {
		int radius = 1500;

		URI uri = UriComponentsBuilder.fromUriString(kakaoApiUrl)
			.queryParam("query", "하나은행")
			.queryParam("y", lat)
			.queryParam("x", lng)
			.queryParam("radius", radius)
			.queryParam("category_group_code", "BK9")
			.queryParam("sort", "distance")
			.encode()
			.build()
			.toUri();

		return fetchBranches(uri);

	}

	private Mono<List<BranchResponseDto>> searchBranchesByQuery(String query, Double lat, Double lng) {
		int radius = 100000;

		URI uri = UriComponentsBuilder.fromUriString(kakaoApiUrl)
			.queryParam("query", "하나은행 " + query)
			.queryParam("category_group_code", "BK9")
			.queryParam("y", lat)
			.queryParam("x", lng)
			.queryParam("radius", radius)
			.encode()
			.build()
			.toUri();

		return fetchBranches(uri);
	}

	private Mono<List<BranchResponseDto>> fetchBranches(URI uri) {
		return webClient.get()
			.uri(uri)
			.header("Authorization", "KakaoAK " + kakaoApiKey)
			.retrieve()
			.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
				clientResponse -> Mono.error(
					new ApiRequestException("Kakao API error: " + clientResponse.statusCode())))
			.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
			})
			.map(response -> {
				List<Map<String, Object>> documents = (List<Map<String, Object>>)response.get("documents");

				if (documents == null) {
					documents = List.of();
				}

				return documents.stream()
					.filter(branch -> branch.get("place_name").toString().contains("하나은행"))
					.map(BranchResponseMapper::toDto)
					.toList();

			});
	}

	private void validateSearchParams(Double lat, Double lng) {
		if (lat == null || lng == null) {
			throw new InvalidRequestException("location information (latitude, longitude) is required");
		}
		if (lat < -90 || lat > 90) {
			throw new InvalidRequestException("Latitude must be between -90 and 90.");
		}

		if (lng < -180 || lng > 180) {
			throw new InvalidRequestException("Longitude must be between -180 and 180.");
		}
	}

}
