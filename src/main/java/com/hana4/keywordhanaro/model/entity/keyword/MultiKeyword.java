package com.hana4.keywordhanaro.model.entity.keyword;

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

@Entity
@Table(uniqueConstraints = {
	@UniqueConstraint(
		name = "uk_multi_keyword_composition",
		columnNames = {"multi_keyword_id", "keyword_id"}
	)
})
public class MultiKeyword {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "multi_keyword_id", nullable = false, foreignKey = @ForeignKey(name = "fk_MultiKeyword_multi_keyword_id_Keyword"))
	private Keyword multiKeyword;

	@ManyToOne
	@JoinColumn(name = "keyword_id", nullable = false, foreignKey = @ForeignKey(name = "fk_MultiKeyword_keyword_id_Keyword"))
	private Keyword keyword;

	@Column(name = "seq_order", nullable = false)
	private byte seqOrder;

}
