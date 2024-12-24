package com.hana4.keywordhanaro.model.entity.keyword;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(uniqueConstraints = {
	@UniqueConstraint(
		name = "uk_multiKeyword_composition",
		columnNames = {"multiKeywordId", "keywordId"}
	)
})
public class MultiKeyword {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
	private Long id;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "multiKeywordId", nullable = false, foreignKey = @ForeignKey(name = "fk_MultiKeyword_multiKeywordId_Keyword"))
	private Keyword multiKeyword;

	@ManyToOne
	@JoinColumn(name = "keywordId", nullable = false, foreignKey = @ForeignKey(name = "fk_MultiKeyword_keywordId_Keyword"))
	private Keyword keyword;

	@Column(name = "seqOrder", nullable = false)
	private byte seqOrder;
}
