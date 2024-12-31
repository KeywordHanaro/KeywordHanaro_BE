package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.model.dto.ChatReqDto;
import com.hana4.keywordhanaro.model.entity.user.User;

public interface LLMService {
	// String getInfo();

	ChatReqDto chat(ChatReqDto chatReqDTO, User user);
}
