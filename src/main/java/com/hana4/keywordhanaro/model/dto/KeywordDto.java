package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeywordDto {
	private Long id;
	private User user;
	private String type;
	private String name;
	private Boolean isFavorite;
	private String desc;
	private Long seqOrder;
	private Account accountId;
	private Account subAccountId;
	private String inquiryWord;
	private Boolean checkEveryTime;
	private BigDecimal amount;
	private String groupMember;
	private String branch;
	// private String fromAccount;
	// private String toAccount;
}
