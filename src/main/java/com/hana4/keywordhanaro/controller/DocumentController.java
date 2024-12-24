package com.hana4.keywordhanaro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.DocumentDto;
import com.hana4.keywordhanaro.model.entity.document.DocumentType;
import com.hana4.keywordhanaro.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
@Tag(name = "Document", description = "문서 관리 API")
public class DocumentController {
	private final DocumentService documentService;

	@Operation(
		summary = "문서 전송",
		description = "문서 타입에 따라 문서를 전송합니다")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "문서 전송 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(type = "string", example = "type document completed"))),
		@ApiResponse(
			responseCode = "400",
			description = "송금 서류의 경우 Amount가 없거나, 입금 서류의 경우 ",
			content = @Content(mediaType = "application/json", schema = @Schema(
				example = "{ \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Error description\" }"
			)))})
	@PostMapping("/{type}")
	public ResponseEntity<String> transfer(
		@Parameter(description = "문서 타입 (대소문자 구분 없음)")
		@PathVariable String type,
		@Parameter(description = "문서 정보", required = true, schema = @Schema(implementation = DocumentDto.class))
		@RequestBody DocumentDto documentDto) {
		documentDto.setType(DocumentType.valueOf(type.toUpperCase()));
		documentService.addDocument(documentDto);
		return ResponseEntity.ok(type + " document completed");
	}
}
