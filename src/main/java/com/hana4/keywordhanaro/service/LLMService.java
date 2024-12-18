package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.model.dto.ChatDto;

public interface LLMService {
	public String getInfo();

	String chat(ChatDto chatDTO);
}
