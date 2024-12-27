package com.hana4.keywordhanaro.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class LLMQueryResDto {
	private String query;
	private String answer;
}
