package com.hana4.keywordhanaro.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionStatus;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionType;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
		private final AccountRepository accountRepository;
		private final TransactionRepository transactionRepository;

		@Transactional
		public Transaction transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
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
				// 계좌 잔액 업데이트
				BigDecimal fromAccountAfterBalance = fromAccount.getBalance().subtract(amount);
				BigDecimal toAccountAfterBalance = toAccount.getBalance().add(amount);

				// 계좌 잔액 업데이트
				fromAccount.setBalance(fromAccountAfterBalance);
				toAccount.setBalance(toAccountAfterBalance);

				accountRepository.save(fromAccount);
				accountRepository.save(toAccount);

				// Transaction 객체 생성 및 반환
				Transaction transaction = new Transaction();
				transaction.setAccount(fromAccount);
				transaction.setSubAccount(toAccount);
				transaction.setAmount(amount);
				transaction.setBeforeBalance(fromAccount.getBalance()); // 송금 전 잔액
				transaction.setAfterBalance(fromAccountAfterBalance); // 송금 후 잔액
				transaction.setCreateAt(LocalDateTime.now());
				transaction.setStatus(TransactionStatus.SUCCESS);
				transaction.setType(TransactionType.WITHDRAW);

				return transactionRepository.save(transaction);
		}
}
