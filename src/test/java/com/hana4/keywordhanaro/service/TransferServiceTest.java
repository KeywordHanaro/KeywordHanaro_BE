package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TransferServiceTest {
    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testTransfer() {
        // 초기 상태 확인
        Account fromAccount = accountRepository.findByAccountNumber("1");
        Account toAccount = accountRepository.findByAccountNumber("2");
        BigDecimal initialFromBalance = fromAccount.getBalance();
        BigDecimal initialToBalance = toAccount.getBalance();

        // 송금 실행 todo 이 과정이 성공시, transaction테이블에 데이터로 들어와야함.
        transferService.transfer("1", "2", BigDecimal.valueOf(500));

        // 업데이트된 상태 확인
        Account updatedFromAccount = accountRepository.findByAccountNumber("1");
        Account updatedToAccount = accountRepository.findByAccountNumber("2");

        assertEquals(initialFromBalance.subtract(BigDecimal.valueOf(500)), updatedFromAccount.getBalance());
        assertEquals(initialToBalance.add(BigDecimal.valueOf(500)), updatedToAccount.getBalance());
    }

    @Test
    public void transferInsufficientBalanceTest() {
        // 초기 상태 확인
        Account fromAccount = accountRepository.findByAccountNumber("1");
        Account toAccount = accountRepository.findByAccountNumber("2");
        BigDecimal initialFromBalance = fromAccount.getBalance();
        BigDecimal initialToBalance = toAccount.getBalance();

        // 잔액을 초과하는 금액으로 송금 시도
        BigDecimal excessAmount = new BigDecimal("1000000");

        // IllegalArgumentException이 발생하는지 확인
        assertThrows(IllegalArgumentException.class, () -> {
            transferService.transfer("1", "2", excessAmount);
        });

        // 계좌 잔액 확인
        Account updatedFromAccount = accountRepository.findByAccountNumber("1");
        Account updatedToAccount = accountRepository.findByAccountNumber("2");

        assertEquals(initialFromBalance, updatedFromAccount.getBalance());
        assertEquals(initialToBalance, updatedToAccount.getBalance());

    }

    @Test
    public void checkTransferFromSavingAccountTest(){
        Account savingsAccount = accountRepository.findById(11L).orElseThrow(()->
                new IllegalArgumentException("11번 계좌가 존재하지 않습니다."));
        // 일반 예금 계좌 설정 (id = 1)
        Account targetAccount = accountRepository.findById(1L).orElseThrow(() ->
                new IllegalArgumentException("Account with id 1 not found"));

        BigDecimal transferAmount = BigDecimal.valueOf(5000);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transferService.transfer(savingsAccount.getAccountNumber(), targetAccount.getAccountNumber(), transferAmount);
        });
        // 예외 메시지 확인
        assertEquals("적금 계좌는 타 계좌로의 송금이 불가합니다.", exception.getMessage());

        // 송금이 실패했으므로 잔액이 변경되지 않았는지 검증
        Account updatedSavingsAccount = accountRepository.findById(11L).orElseThrow();
        Account updatedTargetAccount = accountRepository.findById(1L).orElseThrow();

        assertEquals(savingsAccount.getBalance(), updatedSavingsAccount.getBalance());
        assertEquals(targetAccount.getBalance(), updatedTargetAccount.getBalance());
    }
}

