package com.hana4.keywordhanaro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana4.keywordhanaro.model.entity.Bank;
import com.hana4.keywordhanaro.model.entity.account.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByAccountNumber(String accountNumber);

	List<Account> findAllByUserUsername(String username);

	Optional<Account> findByAccountNumberAndBank(String accountNumber, Bank bank);
}
