package com.hana4.keywordhanaro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	Optional<Keyword> findByName(String name);

	Optional<Keyword> findTopByUserIdOrderBySeqOrderDesc(String userId);

	List<Keyword> findByUserId(String useId);

	Optional<Keyword> findByUserIdAndType(String userId, KeywordType type);
}
