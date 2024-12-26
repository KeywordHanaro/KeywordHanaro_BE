package com.hana4.keywordhanaro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.BranchDto;
import com.hana4.keywordhanaro.service.BranchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/branch")
@Tag(name = "Branch", description = "영업점 검색 API")
public class BranchController {
	private final BranchService branchService;

	@Operation(
		summary = "영업점 검색",
		description = "영업점명, 주소를 입력하거나 사용자 위치 기반으로 영업점을 검색합니다.",
		parameters = {
			@Parameter(name = "query", description = "검색 키워드", example = "성수"),
			@Parameter(name = "y", description = "위도", required = true, example = "37.5445598"),
			@Parameter(name = "x", description = "경도", required = true, example = "127.0560563")
		}
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "영업점 검색 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청(검색어 또는 위치 값이 누락, 위도, 경도 범위 오류)",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Error description\" }"
			))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"server error message\" }"
			)))
	})
	@GetMapping("/search")
	public ResponseEntity<List<BranchDto>> getSearchBranches(
		@RequestParam(value = "query", required = false) String query,
		@RequestParam(value = "y") Double lat,
		@RequestParam(value = "x") Double lng
	) {
		return ResponseEntity.ok(branchService.searchBranch(query, lat, lng));

	}
}
