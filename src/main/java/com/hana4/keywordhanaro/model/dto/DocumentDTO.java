package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DocumentDTO extends BaseDto {

	private Long id;
	private DocumentType type;
	private String fromAccount;
	private String toAccount;
	private BigDecimal amount;

	// Getters and setters

	public enum DocumentType {
		TRANSFER("송금"),
		DEPOSIT("입금"),
		WITHDRAW("출금");

		private final String description;

		DocumentType(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
	}
}
