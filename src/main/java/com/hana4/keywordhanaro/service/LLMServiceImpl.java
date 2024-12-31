package com.hana4.keywordhanaro.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.ChatReqDto;
import com.hana4.keywordhanaro.model.dto.LLMQueryResDto;
import com.hana4.keywordhanaro.model.entity.Chat;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.mapper.ChatMapper;
import com.hana4.keywordhanaro.repository.ChatRepository;

@Service
public class LLMServiceImpl implements LLMService {
	@Value("${LLM_SERVER_URL}")
	private String LLM_SERVER_URL;

	private final RestTemplate restTemplate = new RestTemplate();

	private final ChatRepository chatRepository;

	public LLMServiceImpl(ChatRepository chatRepository) {
		this.chatRepository = chatRepository;
	}

	// @Override
	// public String getInfo() {
	// 	String apiUrl = LLM_SERVER_URL + "/getInfo";
	// 	ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
	// 	return response;
	// }

	@Override
	public ChatReqDto chat(ChatReqDto chatReqDTO, User user) {
		String apiUrl = LLM_SERVER_URL + "/chat";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		String jsonBody = String.format("{\"query\":\"%s\"}", chatReqDTO.getQuestion());

		HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			LLMQueryResDto queryResponse = objectMapper.readValue(response.getBody(), LLMQueryResDto.class);
			chatReqDTO.setAnswer(queryResponse.getAnswer());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Chat chat;
		chat = ChatMapper.toChat(chatReqDTO, user);

		return ChatMapper.toDTO(chatRepository.save(chat));
	}
}
