package com.hana4.keywordhanaro.model.entity.keyword;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
// @ToString(exclude = {"multiKeywords"})
// @EqualsAndHashCode(exclude = {"multiKeywords"})
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

	@OneToMany(mappedBy = "multiKeyword", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference // 순환 참조 방지
	private List<MultiKeyword> multiKeywords = new ArrayList<>();

	public void addMultiKeyword(MultiKeyword multiKeyword) {
		if (this.multiKeywords == null) {
			this.multiKeywords = new ArrayList<>();
		}
		this.multiKeywords.add(multiKeyword);
		multiKeyword.setMultiKeyword(this);
	}

	public void removeMultiKeyword(MultiKeyword multiKeyword) {
		this.multiKeywords.remove(multiKeyword);
		multiKeyword.setMultiKeyword(null);
	}

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

	// multi keyword
	public Keyword(User user, KeywordType type, String name, String description, Long seqOrder
	) {
		this.user = user;
		this.type = type;
		this.name = name;
		this.description = description;
		this.seqOrder = seqOrder;
	}

	public Keyword(Long id, User user, String name, KeywordType type, String description) {
		this.id = id;
		this.user = user;
		this.name = name;
		this.type = type;
		this.description = description;
	}

}
