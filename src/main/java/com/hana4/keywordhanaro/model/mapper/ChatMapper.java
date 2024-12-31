package com.hana4.keywordhanaro.model.mapper;

import com.hana4.keywordhanaro.model.dto.ChatReqDto;
import com.hana4.keywordhanaro.model.entity.Chat;
import com.hana4.keywordhanaro.model.entity.user.User;

public class ChatMapper {
	public static ChatReqDto toDTO(Chat chat) {
		return ChatReqDto.builder()
			.answer(chat.getAnswer())
			.question(chat.getQuestion())
			.build();
	}

	public static Chat toChat(ChatReqDto chatReqDto, User user) {
		return new Chat(user, chatReqDto.getQuestion(), chatReqDto.getAnswer());
	}

	// public static Chat toChat(ChatReqDto chatReqDTO, User user) {
	// 	return new Chat(user, chatReqDTO.getQuestion(), chatReqDTO.getAnswer());
	// }
}
