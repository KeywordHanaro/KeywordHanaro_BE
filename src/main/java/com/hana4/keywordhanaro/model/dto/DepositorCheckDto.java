package com.hana4.keywordhanaro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DepositorCheckDto {
	private String accountNumber;
	private Short bankId;
}
