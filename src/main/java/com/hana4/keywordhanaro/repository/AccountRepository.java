package com.hana4.keywordhanaro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana4.keywordhanaro.model.entity.account.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
<<<<<<< HEAD
    Account findByAccountNumber(String accountNumber);
=======
	Optional<Account> findByAccountNumber(String accountNumber);
>>>>>>> bf2adaf (inquiry repository 수정, controller repository test 완료)
}
