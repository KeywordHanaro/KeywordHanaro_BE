package com.hana4.keywordhanaro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hana4.keywordhanaro.model.entity.keyword.Keyword;
import com.hana4.keywordhanaro.model.entity.keyword.KeywordType;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

	// @EntityGraph(attributePaths = {"multiKeyword"})
	// Optional<Keyword> findByIdWithKeyword(Long id);

	@Query("SELECT k FROM Keyword k LEFT JOIN FETCH k.multiKeywords WHERE k.id = :id")
	Optional<Keyword> findByIdWithMultiKeywords(@Param("id") Long id);

	Optional<Keyword> findByName(String name);

	Optional<Keyword> findTopByUserIdOrderBySeqOrderDesc(String userId);

	List<Keyword> findByUserId(String useId);

	Optional<Keyword> findTopByUserIdAndType(String userId, KeywordType type);

	@Query("SELECT DISTINCT k.type FROM Keyword k WHERE k.user.id = :userId")
	List<KeywordType> findTypesByUserId(@Param("userId") String userId);
}
