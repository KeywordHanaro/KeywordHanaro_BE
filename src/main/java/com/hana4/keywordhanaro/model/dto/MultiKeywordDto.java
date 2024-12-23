package com.hana4.keywordhanaro.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
public class MultiKeywordDto {
	private Long id;
	@JsonBackReference
	private KeywordDto multiKeyword;
	private KeywordDto keyword;
	private byte seqOrder;
}
