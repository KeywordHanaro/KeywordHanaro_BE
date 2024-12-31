package com.hana4.keywordhanaro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateMultiKeywordDto {
	private Long keywordId;
	private Long seqOrder;
}
