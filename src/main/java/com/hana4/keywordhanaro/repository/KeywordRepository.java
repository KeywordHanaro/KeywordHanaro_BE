package com.hana4.keywordhanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana4.keywordhanaro.model.entity.keyword.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
}
