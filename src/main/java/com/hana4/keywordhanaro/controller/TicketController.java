package com.hana4.keywordhanaro.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.TicketDto;
import com.hana4.keywordhanaro.model.dto.TicketRequestDto;
import com.hana4.keywordhanaro.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket")
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
public class TicketController {
	private final TicketService ticketService;

	@Operation(
		summary = "번호표 생성",
		description = "지점별 번호표를 생성합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "번호표가 성공적으로 생성되었습니다.",
			content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (필수 필드 누락, 잘못된 데이터 등)",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Invalid request data.\" }"
			))),
		@ApiResponse(responseCode = "404", description = "사용자 또는 키워드를 찾을 수 없음",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 404, \"error\": \"Not Found\", \"message\": \"User or Keyword not found.\" }"
			))),
		@ApiResponse(responseCode = "500", description = "서버 오류",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"An unexpected error occurred.\" }"
			)))
	})
	@PostMapping()
	public ResponseEntity<TicketDto> createTicket(@RequestBody TicketRequestDto ticketRequestDTO) throws IOException {
		return ResponseEntity.ok(ticketService.createTicket(ticketRequestDTO));
	}
}
