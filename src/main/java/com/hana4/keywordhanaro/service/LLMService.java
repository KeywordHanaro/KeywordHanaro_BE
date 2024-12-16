package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.model.dto.ChatDTO;

public interface LLMService {
	public String getInfo();

	String chat(ChatDTO chatDTO);
}
