package com.hana4.keywordhanaro.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hana4.keywordhanaro.model.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
	private Long id;

	@OneToOne
	@JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "fk_Ticket_userId_User"))
	@JsonBackReference
	private User user;

	@Column(name = "branchId", nullable = false)
	private Long branchId;

	@Column(name = "branchName", nullable = false)
	private String branchName;

	@Column(name = "waitingNumber", nullable = false)
	private Long waitingNumber;

	@Column(name = "waitingGuest", nullable = false)
	private Long waitingGuest;

	@Column(name = "workNumber", nullable = false)
	private byte workNumber;

	@CreationTimestamp
	@Column(nullable = false, updatable = false, columnDefinition = "timestamp")
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime createAt;

	public Ticket(Long branchId, String branchName, User user, Long waitingGuest, Long waitingNumber, byte workNumber) {
		this.branchId = branchId;
		this.branchName = branchName;
		this.user = user;
		this.waitingGuest = waitingGuest;
		this.waitingNumber = waitingNumber;
		this.workNumber = workNumber;
	}
}
