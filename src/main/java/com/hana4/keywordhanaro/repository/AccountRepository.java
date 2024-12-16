package com.hana4.keywordhanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana4.keywordhanaro.model.entity.account.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
