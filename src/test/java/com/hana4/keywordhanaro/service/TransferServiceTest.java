package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionStatus;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferServiceTest {
    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MockMvc mockMvc;
    //    @Test
    //    public void testTransfer() {
    //        // 초기 상태 확인
    //        Account fromAccount = accountRepository.findByAccountNumber("111-222-3331");
    //        Account toAccount = accountRepository.findByAccountNumber("32476762224");
    //        BigDecimal initialFromBalance = fromAccount.getBalance();
    //        BigDecimal initialToBalance = toAccount.getBalance();
    //
    //        // 송금 실행 todo 이 과정이 성공시, transaction테이블에 데이터로 들어와야함.
    //        transferService.transfer("111-222-3331", "32476762224", BigDecimal.valueOf(999));
    //
    //        // 업데이트된 상태 확인
    //        Account updatedFromAccount = accountRepository.findByAccountNumber("111-222-3331");
    //        Account updatedToAccount = accountRepository.findByAccountNumber("32476762224");
    //
    //        assertEquals(initialFromBalance.subtract(BigDecimal.valueOf(999)), updatedFromAccount.getBalance());
    //        assertEquals(initialToBalance.add(BigDecimal.valueOf(999)), updatedToAccount.getBalance());
    //    }
    @Test
    public void testTransferSuccess() {
        // Arrange
        String fromAccountNumber = "111-222-3331";
        String toAccountNumber = "32476762224";
        BigDecimal amount = BigDecimal.valueOf(999);

        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);

        assertNotNull(fromAccount, "From account must not be null");
        assertNotNull(toAccount, "To account must not be null");

        BigDecimal initialFromBalance = fromAccount.getBalance();
        BigDecimal initialToBalance = toAccount.getBalance();

        // Act
        Transaction transaction = transferService.transfer(fromAccountNumber, toAccountNumber, amount);

        // Assert
        assertNotNull(transaction, "Transaction should not be null");
        assertEquals(TransactionStatus.SUCCESS, transaction.getStatus(), "Transaction status should be SUCCESS");

        Account updatedFromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account updatedToAccount = accountRepository.findByAccountNumber(toAccountNumber);

        assertEquals(initialFromBalance.subtract(amount), updatedFromAccount.getBalance(), "From account balance mismatch");
        assertEquals(initialToBalance.add(amount), updatedToAccount.getBalance(), "To account balance mismatch");
    }

    @Test
    public void transferInsufficientBalanceTest() {
        // 초기 상태 확인
        Account fromAccount = accountRepository.findByAccountNumber("111-222-3331");
        Account toAccount = accountRepository.findByAccountNumber("111-222-3332");
        BigDecimal initialFromBalance = fromAccount.getBalance();
        BigDecimal initialToBalance = toAccount.getBalance();

        // 잔액을 초과하는 금액으로 송금 시도
        BigDecimal excessAmount = new BigDecimal("10000000000");

        // IllegalArgumentException이 발생하는지 확인
        assertThrows(IllegalArgumentException.class, () -> {
            transferService.transfer("111-222-3331", "111-222-3332", excessAmount);
        });

        // 계좌 잔액 확인
        Account updatedFromAccount = accountRepository.findByAccountNumber("111-222-3331");
        Account updatedToAccount = accountRepository.findByAccountNumber("111-222-3332");

        assertEquals(initialFromBalance, updatedFromAccount.getBalance());
        assertEquals(initialToBalance, updatedToAccount.getBalance());

    }

    @Test
    public void checkTransferFromSavingAccountTest(){
        Account savingsAccount = accountRepository.findById(8L).orElseThrow(()->
            new IllegalArgumentException("8번 계좌가 존재하지 않습니다."));
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
        Account updatedSavingsAccount = accountRepository.findById(8L).orElseThrow();
        Account updatedTargetAccount = accountRepository.findById(1L).orElseThrow();

        assertEquals(savingsAccount.getBalance(), updatedSavingsAccount.getBalance());
        assertEquals(targetAccount.getBalance(), updatedTargetAccount.getBalance());
    }
}
