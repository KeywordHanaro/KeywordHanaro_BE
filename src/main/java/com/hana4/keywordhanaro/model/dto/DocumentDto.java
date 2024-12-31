package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import com.hana4.keywordhanaro.model.entity.document.DocumentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto extends BaseDto {
	private Long id;
	private DocumentType type;
	private AccountDto account;
	private AccountDto subAccount;
	private BigDecimal amount;
}
