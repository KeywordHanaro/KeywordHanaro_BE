package com.hana4.keywordhanaro.model.entity.account;

import java.math.BigDecimal;

import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.BaseEntity;
import com.hana4.keywordhanaro.model.entity.User;

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
public class Account extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
	private Long id;

	@Column(length = 20, nullable = false, unique = true, updatable = false)
	private String accountNumber;

	@ManyToOne
	@JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "fk_Account_userId_User"))
	private User user;

	@ManyToOne
	@JoinColumn(name = "bankId", nullable = false, foreignKey = @ForeignKey(name = "fk_Account_bankId_Bank"))
	private Bank bank;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal balance;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal transferLimit;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountType type;

	@Column(nullable = false)
	private Boolean isMine;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountStatus status;

	public Account(String accountNumber, User user, Bank bank, String name, String password, BigDecimal balance,
		BigDecimal transferLimit, AccountType type, Boolean isMine, AccountStatus status) {
		this.accountNumber = accountNumber;
		this.user = user;
		this.bank = bank;
		this.name = name;
		this.password = password;
		this.balance = balance;
		this.transferLimit = transferLimit;
		this.type = type;
		this.isMine = isMine;
		this.status = status;
	}
}
