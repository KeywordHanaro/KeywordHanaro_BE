package com.hana4.keywordhanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hana4.keywordhanaro.model.entity.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Short> {
}
