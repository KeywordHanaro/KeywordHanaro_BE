package com.hana4.keywordhanaro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.BranchResponseDto;
import com.hana4.keywordhanaro.service.BranchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/branch")
public class BranchController {
	private final BranchService branchService;

	@Operation(
		summary = "영업점 검색",
		description = "영업점명, 주소를 입력하거나 사용자 위치 기반으로 영업점을 검색합니다",
		parameters = {
			@Parameter(name = "query", description = "검색 키워드", example = "성수"),
			@Parameter(name = "y", description = "위도", required = true, example = "37.5445598"),
			@Parameter(name = "x", description = "경도", required = true, example = "127.0560563")
		}
	)
	@GetMapping("/search")
	public Mono<List<BranchResponseDto>> getSearchBranches(
		@RequestParam(value = "query", required = false) String query,
		@RequestParam(value = "y") Double lat,
		@RequestParam(value = "x") Double lng
	) {
		return branchService.searchBranch(query, lat, lng);

	}
}
