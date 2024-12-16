package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferServiceImpl implements TransferService{
    private final AccountRepository accountRepository;

    public TransferServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public double getInitialBalance() {
        Account account = accountRepository.findByAccountNumber("1");
        return account != null ? account.getBalance().doubleValue() : 0.0;
    }

    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount){
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Invalid account number");
        }

        if (!fromAccount.canTransfer()) {
            throw new IllegalArgumentException("적금 계좌는 타 계좌로의 송금이 불가합니다.");
        }

        // 잔액 부족 시 처리 로직
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }




}
