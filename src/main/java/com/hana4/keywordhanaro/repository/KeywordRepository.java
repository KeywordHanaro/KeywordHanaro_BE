package com.hana4.keywordhanaro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana4.keywordhanaro.model.entity.keyword.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	Optional<Keyword> findByName(String name);

	Optional<Keyword> findTopByUserIdOrderBySeqOrderDesc(String userId);
}
