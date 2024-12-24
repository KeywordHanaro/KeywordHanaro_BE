package com.hana4.keywordhanaro.service;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.hana4.keywordhanaro.exception.InvalidRequestException;
import com.hana4.keywordhanaro.exception.KakaoApiException;
import com.hana4.keywordhanaro.model.dto.BranchDto;
import com.hana4.keywordhanaro.model.mapper.BranchMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
	@Value("${KAKAO_CLIENT_ID}")
	private String kakaoApiKey;
	@Value("${KAKAO_API_URL}")
	private String kakaoApiUrl;

	private final RestTemplate restTemplate;

	@Override
	public List<BranchDto> searchBranch(String query, Double lat, Double lng) {
		validateSearchParams(lat, lng);

		if (query == null || query.trim().isEmpty()) {
			return searchNearbyBranches(lat, lng);
		} else {
			return searchBranchesByQuery(query, lat, lng);
		}

	}

	private List<BranchDto> searchNearbyBranches(Double lat, Double lng) {
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

	private List<BranchDto> searchBranchesByQuery(String query, Double lat, Double lng) {
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

	private List<BranchDto> fetchBranches(URI uri) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + kakaoApiKey);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
				uri, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {
				}
			);
			Map<String, Object> body = response.getBody();
			List<Map<String, Object>> documents = (List<Map<String, Object>>)body.get("documents");

			return documents.stream()
				.filter(branch -> {
					String placeName = branch.get("place_name").toString();
					return placeName.contains("하나은행") &&
						!placeName.contains("ATM") &&
						!placeName.contains("하나은행365");
				})
				.map(BranchMapper::toDto)
				.collect(Collectors.toList());
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new KakaoApiException("Kakao API error: " + e.getStatusCode());
		}
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
