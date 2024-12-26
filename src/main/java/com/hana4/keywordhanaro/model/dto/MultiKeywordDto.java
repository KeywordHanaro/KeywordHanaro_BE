package com.hana4.keywordhanaro.model.dto;

import java.util.List;

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
	private Long parentId;
	private KeywordDto keyword;
	private Long seqOrder;
}
