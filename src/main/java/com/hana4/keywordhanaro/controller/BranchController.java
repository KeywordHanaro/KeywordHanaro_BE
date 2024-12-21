package com.hana4.keywordhanaro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.BranchResponseDto;
import com.hana4.keywordhanaro.service.BranchService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/branch")
public class BranchController {
	private final BranchService branchService;

	@GetMapping("/search")
	public Mono<List<BranchResponseDto>> getSearchBranches(
		@RequestParam(value = "query", required = false) String query,
		@RequestParam("y") double lat,
		@RequestParam("x") double lng
	) {
		int radius = 1500;

		if (query == null || query.isBlank()) {
			query = "성수";
		}

		return branchService.searchBranches(query, lat, lng, radius);

	}
}
