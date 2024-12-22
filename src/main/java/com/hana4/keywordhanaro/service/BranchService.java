package com.hana4.keywordhanaro.service;

import java.util.List;

import com.hana4.keywordhanaro.model.dto.BranchDto;

import reactor.core.publisher.Mono;

public interface BranchService {

	Mono<List<BranchDto>> searchBranch(String query, Double lat, Double lng);

}
