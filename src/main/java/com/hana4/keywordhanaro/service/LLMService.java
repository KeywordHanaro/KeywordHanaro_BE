package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.model.dto.ChatReqDto;
import com.hana4.keywordhanaro.model.dto.UserDto;

public interface LLMService {
	// String getInfo();

	ChatReqDto chat(ChatReqDto chatReqDTO, UserDto userDto);
}
