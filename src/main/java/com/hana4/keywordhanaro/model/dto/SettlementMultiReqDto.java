package com.hana4.keywordhanaro.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SettlementMultiReqDto {
	private String code;
	private List<SettlementReqDto> settlementList;
}
