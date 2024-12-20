package com.hana4.keywordhanaro.model.entity.keyword;

import java.math.BigDecimal;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@ToString
public class Keyword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "fk_Keyword_userId_User"))
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private KeywordType type;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "isFavorite", nullable = false)
	private boolean isFavorite = false;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "seqOrder", nullable = false)
	private Long seqOrder;

	private String inquiryWord;

	private Boolean checkEveryTime;

	@Column(precision = 15, scale = 2)
	private BigDecimal amount;

	@Column(columnDefinition = "JSON")
	private String groupMember;

	@Column(columnDefinition = "JSON")
	private String branch;

	@ManyToOne
	@JoinColumn(name = "accountId", foreignKey = @ForeignKey(name = "fk_Keyword_accountId_Account"))
	private Account account;

	@ManyToOne
	@JoinColumn(name = "subAccountId", foreignKey = @ForeignKey(name = "fk_Keyword_subAccountId_Account"))
	private Account subAccount;

	// inquiry keyword
	public Keyword(User user, KeywordType type, String name, String description, Long seqOrder, Account account,
		String inquiryWord) {
		this.user = user;
		this.type = type;
		this.name = name;
		this.description = description;
		this.seqOrder = seqOrder;
		this.account = account;
		this.inquiryWord = inquiryWord;
	}

	// transfer keyword
	public Keyword(User user, KeywordType type, String name, String description, Long seqOrder, Account account,
		Account subAccount, BigDecimal amount, Boolean checkEveryTime) {
		this.user = user;
		this.seqOrder = seqOrder;
		this.name = name;
		this.description = description;
		this.type = type;
		this.subAccount = subAccount;
		this.account = account;
		this.amount = amount;
		this.checkEveryTime = checkEveryTime;
	}

	// settlement keyword
	public Keyword(User user, KeywordType type, String name, String description, Long seqOrder, Account account,
		String groupMember, BigDecimal amount, Boolean checkEveryTime) {
		this.user = user;
		this.type = type;
		this.seqOrder = seqOrder;
		this.name = name;
		this.description = description;
		this.account = account;
		this.groupMember = groupMember;
		this.amount = amount;
		this.checkEveryTime = checkEveryTime;
	}

	// ticket keyword
	public Keyword(User user, KeywordType type, String name, String description, Long seqOrder, String branch) {
		this.user = user;
		this.type = type;
		this.seqOrder = seqOrder;
		this.name = name;
		this.branch = branch;
		this.description = description;
	}

}
