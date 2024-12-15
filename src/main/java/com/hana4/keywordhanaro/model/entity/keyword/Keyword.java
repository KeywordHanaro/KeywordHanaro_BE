package com.hana4.keywordhanaro.model.entity.keyword;

import java.math.BigDecimal;

import org.hibernate.annotations.Comment;

import com.hana4.keywordhanaro.model.entity.User;
import com.hana4.keywordhanaro.model.entity.account.Account;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Keyword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_Keyword_user_id_User"))
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private KeywordType type;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "is_favorite", nullable = false)
	private boolean isFavorite = false;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "seq_order", nullable = false)
	private Long seqOrder;

	@ManyToOne
	@JoinColumn(name = "account_id", foreignKey = @ForeignKey(name="fk_Keyword_account_id_Account"))
	private Account account;

	private String inquiryWord;

	private Boolean checkEveryTime;

	@Column(precision = 8, scale = 2)
	private BigDecimal amount;

	@Column(columnDefinition = "JSON")
	private String groupMember;

	@Column(columnDefinition = "JSON")
	private String branch;

	@ManyToOne
	@JoinColumn(name = "from_account", foreignKey = @ForeignKey(name = "fk_Keyword_from_account_id_Account"))
	private Account fromAccount;

	@ManyToOne
	@JoinColumn(name = "to_account", foreignKey = @ForeignKey(name = "fk_Keyword_to_account_id_Account"))
	private Account toAccount;
}
