package com.hana4.keywordhanaro.model.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ChatDTO {
	private Long id;
	private String userId;
	private String content;
	private String type;
	private Timestamp createdAt;

}
