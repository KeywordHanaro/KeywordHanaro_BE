package com.hana4.keywordhanaro.model.dto;

import java.math.BigDecimal;

import com.hana4.keywordhanaro.model.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KeywordDto {
	private Long id;
	private User user;
	private String type;
	private String name;
	private boolean isFavorite = false;
	private String desc;
	private Long seqOrder;
	private AccountDto account;
	private AccountDto subAccount;
	private String inquiryWord;
	private Boolean checkEveryTime;
	private BigDecimal amount;
	private String groupMember;
	private String branch;

	public KeywordDto(User user, String type, String name, String desc, AccountDto account, String inquiryWord) {
		this.user = user;
		this.type = type;
		this.name = name;
		this.desc = desc;
		this.account = account;
		this.inquiryWord = inquiryWord;
	}
}
