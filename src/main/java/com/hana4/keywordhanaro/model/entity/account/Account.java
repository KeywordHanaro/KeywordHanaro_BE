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
	@Column(length = 20, nullable = false)
	private String id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_Account_user_id_User"))
	private User user;

	@ManyToOne
	@JoinColumn(name = "bank_id", nullable = false, foreignKey = @ForeignKey(name = "fk_Account_bank_id_Bank"))
	private Bank bank;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, precision = 8, scale = 2)
	private BigDecimal balance;

	@Column(nullable = false, precision = 8, scale = 2)
	private BigDecimal transferLimit;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountType type;

	@Column(nullable = false)
	private Boolean mine;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountStatus status;
}
