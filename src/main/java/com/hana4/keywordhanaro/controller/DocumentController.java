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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {
	private final DocumentService documentService;

	@PostMapping("/{type}")
	public ResponseEntity<String> transfer(@PathVariable String type, @RequestBody DocumentDto documentDto) {
		documentDto.setType(DocumentType.valueOf(type.toUpperCase()));
		documentService.addDocument(documentDto);
		return ResponseEntity.ok(type + " document completed");
	}
}
