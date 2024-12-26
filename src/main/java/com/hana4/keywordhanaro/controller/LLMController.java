package com.hana4.keywordhanaro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.ChatDto;
import com.hana4.keywordhanaro.service.LLMService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/llm")
@Tag(name = "LLM", description = "LLM 관련 API")
public class LLMController {
	private final LLMService llmService;

	public LLMController(LLMService llmService) {
		this.llmService = llmService;
	}

	@GetMapping("/getInfo")
	public String getInfo() {
		return llmService.getInfo();
	}

	@Operation(summary = "LLM 채팅", description = "LLM과 채팅을 시작합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Invalid chat input.\" }"))),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"server error message\" }")))
	})
	@PostMapping("/chat")
	public String chat(@RequestBody ChatDto chatDTO) {
		return llmService.chat(chatDTO);
	}

}
