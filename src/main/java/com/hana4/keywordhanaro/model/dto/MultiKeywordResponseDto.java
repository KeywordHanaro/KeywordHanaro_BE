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
public class MultiKeywordResponseDto {
	private Long id;
	private Long parentId;
	private KeywordResponseDto keyword;
	private Long seqOrder;
}
