package com.hana4.keywordhanaro.model.entity.user;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hana4.keywordhanaro.model.entity.BaseEntity;
import com.hana4.keywordhanaro.model.entity.Ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(uniqueConstraints = {
	@UniqueConstraint(
		name = "uniq_User_username",
		columnNames = {"username"}
	)})
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(length = 36)
	private String id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	private String masterPassword;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserStatus status;

	private String email;

	private String tel;

	private int permission;

	@Column(length = 36)
	private String kakaoUUID;

	@OneToOne(mappedBy = "user")
	@JsonManagedReference
	private Ticket ticket;

	public User(String username, String password, String name, UserStatus status, int permission) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.status = status;
		this.permission = permission;
	}

	public User(String id, String username, String password, String name, UserStatus status, String email, String tel,
		int permission, Ticket ticket, LocalDateTime createAt, LocalDateTime updateAt) {
		super(createAt, updateAt);
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.status = status;
		this.email = email;
		this.tel = tel;
		this.permission = permission;
		this.ticket = ticket;
	}

	public User(String id, String name, UserStatus status, int permission) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.permission = permission;
	}

	public User(String username, String password, String name,
		UserStatus status, String email, String tel, int permission, String kakaoUUID) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.status = status;
		this.email = email;
		this.tel = tel;
		this.permission = permission;
		this.kakaoUUID = kakaoUUID;
	}

	public User(String username, String password, String masterPassword,
		String name, UserStatus status, String email, String tel, int permission, String kakaoUUID) {
		this.username = username;
		this.password = password;
		this.masterPassword = masterPassword;
		this.name = name;
		this.status = status;
		this.email = email;
		this.tel = tel;
		this.permission = permission;
		this.kakaoUUID = kakaoUUID;
	}

	public User(String username, String password, String name,
		UserStatus status, String email, String tel, int permission) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.status = status;
		this.email = email;
		this.tel = tel;
		this.permission = permission;
	}
}
