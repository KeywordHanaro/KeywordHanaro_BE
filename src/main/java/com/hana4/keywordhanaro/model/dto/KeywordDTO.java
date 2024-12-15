package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class KeywordDTO {
	private Long id;
	private String userId;
	private String type;
	private String name;
	private Boolean isFavorite;
	private String desc;
	private String accountId;
	private String inquiryWord;
	private Boolean checkEveryTime;
	private BigDecimal amount;
	private String groupMember;
	private String branch;
	private String fromAccount;
	private String toAccount;
}
