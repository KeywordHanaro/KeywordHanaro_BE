package com.hana4.keywordhanaro.model.mapper;

import java.sql.Timestamp;

import com.hana4.keywordhanaro.model.dto.ChatDto;
import com.hana4.keywordhanaro.model.entity.Chat;
import com.hana4.keywordhanaro.model.entity.user.User;

public class ChatMapper {
	public static ChatDto toDTO(Chat chat) {
		return ChatDto.builder()
			.id(chat.getId())
			.userId(chat.getUser().getId())
			.question(chat.getQuestion())
			.answer(chat.getAnswer())
			.createdAt(Timestamp.valueOf(chat.getCreateAt()))
			.build();
	}

	public static Chat toChat(ChatDto chatDTO, User user) {
		return new Chat(user, chatDTO.getQuestion(), chatDTO.getAnswer());
	}
}
