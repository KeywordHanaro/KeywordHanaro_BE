package com.hana4.keywordhanaro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana4.keywordhanaro.model.dto.ChatDto;
import com.hana4.keywordhanaro.service.LLMService;

@RestController
@RequestMapping("/llm")
public class LLMController {
	private final LLMService llmService;

	public LLMController(LLMService llmService) {
		this.llmService = llmService;
	}

	@GetMapping("/getInfo")
	public String getInfo() {
		return llmService.getInfo();
	}

	@PostMapping("/chat")
	public String chat(@RequestBody ChatDto chatDTO) {
		return llmService.chat(chatDTO);
	}

}
