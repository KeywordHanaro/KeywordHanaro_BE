package com.hana4.keywordhanaro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SettlementReqDto {
	private String code;
	private KeywordDto formData;
}
