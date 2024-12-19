package com.hana4.keywordhanaro.repository;

import com.hana4.keywordhanaro.model.entity.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);
    // Optional<Account> findByAccountNumber(String accountNumber);
}
