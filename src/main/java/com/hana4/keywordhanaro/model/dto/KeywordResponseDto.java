package com.hana4.keywordhanaro.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordResponseDto {
	private KeywordDto keywordDto;
	private List<TransactionDto> transactions;

}
