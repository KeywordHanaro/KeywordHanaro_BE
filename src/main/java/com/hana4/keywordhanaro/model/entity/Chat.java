package com.hana4.keywordhanaro.model.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

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
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_Chat_user_id_User"))
	private User user;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ChatType type;

	@CreationTimestamp
	@Column(nullable = false, updatable = false, columnDefinition = "timestamp")
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime createAt;

}
