package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import com.hana4.keywordhanaro.model.entity.document.DocumentType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DocumentDto extends BaseDto {
	private Long id;
	private DocumentType type;
	private AccountDto account;
	private AccountDto subAccount;
	private BigDecimal amount;
}
