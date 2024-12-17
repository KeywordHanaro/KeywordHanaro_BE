package com.hana4.keywordhanaro.model.entity.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

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
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "accountId", nullable = false, foreignKey = @ForeignKey(name = "fk_Transaction_accountId_Account"))
	private Account account;

	@ManyToOne
	@JoinColumn(name = "subAccountId", foreignKey = @ForeignKey(name = "fk_Transaction_subAccountId_Account"))
	private Account subAccount;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionType type;

	@Column(length = 36)
	private String alias;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal beforeBalance;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal afterBalance;

	@CreationTimestamp
	@Column(nullable = false, updatable = false, columnDefinition = "timestamp")
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime createAt;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionStatus status;
}
