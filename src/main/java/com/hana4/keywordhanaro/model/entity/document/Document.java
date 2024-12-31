package com.hana4.keywordhanaro.model.entity.document;

import java.math.BigDecimal;

import com.hana4.keywordhanaro.model.entity.BaseEntity;
import com.hana4.keywordhanaro.model.entity.account.Account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Document extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private DocumentType type;

	@ManyToOne
	@JoinColumn(name = "accountId", nullable = true)
	private Account account;

	@ManyToOne
	@JoinColumn(name = "subAccountId", nullable = true)
	private Account subAccount;

	@Column(name = "amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal amount;

	public Document(DocumentType type, Account account, Account subAccount, BigDecimal amount) {
		this.type = type;
		this.account = account;
		this.subAccount = subAccount;
		this.amount = amount;
	}
}
