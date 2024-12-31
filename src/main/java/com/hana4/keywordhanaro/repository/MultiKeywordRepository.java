package com.hana4.keywordhanaro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hana4.keywordhanaro.model.entity.keyword.MultiKeyword;

public interface MultiKeywordRepository extends JpaRepository<MultiKeyword, Long> {
	// @EntityGraph(attributePaths = {"writer", "post"})
	// List<Comment> findAllByPostId(Long postId);

	@EntityGraph(attributePaths = {"multiKeyword"})
	List<MultiKeyword> findAllByMultiKeywordId(Long multiKeywordId);

	List<MultiKeyword> findAllByKeywordId(Long multiKeywordId);
}
