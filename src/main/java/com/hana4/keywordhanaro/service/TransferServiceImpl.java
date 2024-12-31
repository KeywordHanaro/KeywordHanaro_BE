package com.hana4.keywordhanaro.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.exception.TransferErrorException;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionStatus;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionType;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {
	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;

	@Transactional
	public Transaction transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) throws Exception {
		Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
			.orElseThrow(() -> new AccountNotFoundException("출금 계좌번호가 존재하지 않습니다."));
		Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
			.orElseThrow(() -> new AccountNotFoundException("입금 계좌번호가 존재하지 않습니다."));

		if (!fromAccount.canTransfer()) {
			logFailedTransaction(fromAccount, toAccount, amount, "적금 계좌 송금 불가");
			throw new TransferErrorException("cannot send from saving account");
		}

		// 잔액 부족 시 처리 로직
		if (fromAccount.getBalance().compareTo(amount) < 0) {
			logFailedTransaction(fromAccount, toAccount, amount, "잔액부족");
			throw new TransferErrorException("not enough money in account");
		}
		// 계좌 잔액 업데이트(이체 로직)
		BigDecimal fromAccountAfterBalance = fromAccount.getBalance().subtract(amount);
		BigDecimal toAccountAfterBalance = toAccount.getBalance().add(amount);

		fromAccount.setBalance(fromAccountAfterBalance);
		toAccount.setBalance(toAccountAfterBalance);

		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);

		// Transaction 객체 생성 및 반환
		transactionRepository.save(new Transaction(toAccount, fromAccount, amount, TransactionType.DEPOSIT,
			toAccount.getBalance().subtract(amount), toAccountAfterBalance, TransactionStatus.SUCCESS, ""));

		return transactionRepository.save(new Transaction(fromAccount, toAccount, amount, TransactionType.WITHDRAW,
			fromAccount.getBalance().add(amount), fromAccountAfterBalance, TransactionStatus.SUCCESS, ""));
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void logFailedTransaction(Account fromAccount, Account toAccount, BigDecimal amount, String reason) {
		// log about failure Transaction
		//        System.err.println("Transaction failed: " + reason);
		log.error("Invalid transactionType: {}", reason);

		transactionRepository.save(new Transaction(fromAccount, toAccount, amount, TransactionType.WITHDRAW,
			fromAccount != null ? fromAccount.getBalance() : BigDecimal.ZERO,
			fromAccount != null ? fromAccount.getBalance() : BigDecimal.ZERO, TransactionStatus.FAILURE, reason));

		transactionRepository.save(new Transaction(toAccount, fromAccount, amount, TransactionType.DEPOSIT,
			toAccount != null ? toAccount.getBalance() : BigDecimal.ZERO,
			toAccount != null ? toAccount.getBalance() : BigDecimal.ZERO, TransactionStatus.FAILURE, reason));
	}
}
