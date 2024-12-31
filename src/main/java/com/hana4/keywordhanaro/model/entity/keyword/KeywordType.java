package com.hana4.keywordhanaro.model.entity.keyword;

public enum KeywordType {
	INQUIRY, SETTLEMENT, TICKET, TRANSFER, DUES, MULTI;

	public static KeywordType fromString(String value) {
		for (KeywordType type : values()) {
			if (type.name().equalsIgnoreCase(value)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid keyword type: " + value);
	}
}
