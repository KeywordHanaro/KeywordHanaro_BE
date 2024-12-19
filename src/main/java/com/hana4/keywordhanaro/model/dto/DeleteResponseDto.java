package com.hana4.keywordhanaro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeleteResponseDto {
	private boolean success;
	private String message;
}
