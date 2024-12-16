package com.hana4.keywordhanaro.model.mapper;

import java.sql.Timestamp;

import com.hana4.keywordhanaro.model.dto.ChatDTO;
import com.hana4.keywordhanaro.model.entity.Chat;
import com.hana4.keywordhanaro.model.entity.user.User;

public class ChatMapper {
	public static ChatDTO toDTO(Chat chat) {
		return ChatDTO.builder()
			.id(chat.getId())
			.userId(chat.getUser().getId())
			.question(chat.getQuestion())
			.answer(chat.getAnswer())
			.createdAt(Timestamp.valueOf(chat.getCreateAt()))
			.build();
	}

	public static Chat toChat(ChatDTO chatDTO, User user) {
		return new Chat(user, chatDTO.getQuestion(), chatDTO.getAnswer());
	}
}
