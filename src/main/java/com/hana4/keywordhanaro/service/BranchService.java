package com.hana4.keywordhanaro.service;

import java.util.List;

import com.hana4.keywordhanaro.model.dto.BranchResponseDto;

import reactor.core.publisher.Mono;

public interface BranchService {
	Mono<List<BranchResponseDto>> searchBranches(String query, double lat, double lng, int radius);
}
