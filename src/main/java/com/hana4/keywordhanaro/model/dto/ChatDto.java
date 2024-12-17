package com.hana4.keywordhanaro.model.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {
	private Long id;
	private String userId;
	private String question;
	private String answer;
	private Timestamp createdAt;

}
