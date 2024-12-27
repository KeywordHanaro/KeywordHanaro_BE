package com.hana4.keywordhanaro.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana4.keywordhanaro.model.dto.ChatDto;
import com.hana4.keywordhanaro.model.dto.LLMQueryResDto;
import com.hana4.keywordhanaro.model.entity.Chat;
import com.hana4.keywordhanaro.model.entity.user.User;
import com.hana4.keywordhanaro.model.mapper.ChatMapper;
import com.hana4.keywordhanaro.repository.ChatRepository;

@Service
public class LLMServiceImpl implements LLMService {
	private final RestTemplate restTemplate = new RestTemplate();

	private final ChatRepository chatRepository;

	public LLMServiceImpl(ChatRepository chatRepository) {
		this.chatRepository = chatRepository;
	}

	@Override
	public String getInfo() {
		String apiUrl = "http://127.0.0.1:8081/llm/getInfo";
		ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
		return response.getBody();
	}

	@Override
	public String chat(ChatDto chatDTO) {
		String apiUrl = "http://127.0.0.1:8081/llm/chat";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		String jsonBody = String.format("{\"query\":\"%s\"}", chatDTO.getQuestion());

		HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

		User user = new User();
		user.setId(chatDTO.getUserId());

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			LLMQueryResDto queryResponse = objectMapper.readValue(response.getBody(), LLMQueryResDto.class);
			chatDTO.setAnswer(queryResponse.getAnswer());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Chat savedChat = chatRepository.save(ChatMapper.toChat(chatDTO, user));

		return chatDTO.getAnswer();
	}
}
