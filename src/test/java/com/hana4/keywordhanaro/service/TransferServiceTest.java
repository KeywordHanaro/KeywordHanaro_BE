package com.hana4.keywordhanaro.service;

import com.hana4.keywordhanaro.exception.AccountNotFoundException;
import com.hana4.keywordhanaro.exception.TransferErrorException;
import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.transaction.Transaction;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionStatus;
import com.hana4.keywordhanaro.model.entity.transaction.TransactionType;
import com.hana4.keywordhanaro.repository.AccountRepository;
import com.hana4.keywordhanaro.repository.TransactionRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

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

    @Test
    public void testTransferSuccess() throws Exception {
        // Arrange
        String fromAccountNumber = "123-321-0905";
        String toAccountNumber = "32476762224";
        BigDecimal amount = BigDecimal.valueOf(100);

        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber).orElseThrow(() -> new NullPointerException("출금 계좌번호가 존재하지 않습니다."));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber).orElseThrow(() -> new NullPointerException("출금 계좌번호가 존재하지 않습니다."));

        assertNotNull(fromAccount, "From account must not be null");
        assertNotNull(toAccount, "To account must not be null");

        BigDecimal initialFromBalance = fromAccount.getBalance();
        BigDecimal initialToBalance = toAccount.getBalance();

        // Act
        Transaction transaction = transferService.transfer(fromAccountNumber, toAccountNumber, amount);

        // Assert
        assertNotNull(transaction, "Transaction should not be null");
        assertEquals(TransactionStatus.SUCCESS, transaction.getStatus(), "Transaction status should be SUCCESS");

        Account updatedFromAccount = accountRepository.findByAccountNumber(fromAccountNumber).orElseThrow(() -> new NullPointerException("출금 계좌번호가 존재하지 않습니다."));
        ;
        Account updatedToAccount = accountRepository.findByAccountNumber(toAccountNumber).orElseThrow(() -> new NullPointerException("출금 계좌번호가 존재하지 않습니다."));
        ;

        assertEquals(initialFromBalance.subtract(amount), updatedFromAccount.getBalance(), "From account balance mismatch");
        assertEquals(initialToBalance.add(amount), updatedToAccount.getBalance(), "To account balance mismatch");
    }

    @Test
    public void transferInsufficientBalanceTest() throws Exception {
        // 초기 상태 확인
        Account fromAccount = accountRepository.findByAccountNumber("111-222-3331").orElseThrow(() -> new NullPointerException("출금 계좌번호가 존재하지 않습니다."));
        Account toAccount = accountRepository.findByAccountNumber("111-222-3332").orElseThrow(() -> new NullPointerException("수취 계좌번호가 존재하지 않습니다."));
        BigDecimal initialFromBalance = fromAccount.getBalance();
        BigDecimal initialToBalance = toAccount.getBalance();

        // 잔액을 초과하는 금액으로 송금 시도
        BigDecimal excessAmount = new BigDecimal("10000000000");

        // 송금 시도 및 결과 확인
        Assertions.assertThatThrownBy(() ->{
            transferService.transfer("111-222-3331", "111-222-3332", excessAmount);}).isInstanceOf(
            TransferErrorException.class).hasMessageContaining("not enough money in account");

        // 실패한 거래 기록 확인
        // assertNotNull(result);
        // assertEquals(TransactionStatus.FAILURE, result.getStatus());
        // assertEquals(fromAccount, result.getAccount());
        // assertEquals(toAccount, result.getSubAccount());
        // assertEquals(excessAmount, result.getAmount());
        // assertEquals(TransactionType.WITHDRAW, result.getType());
        // assertEquals("잔액부족", result.getRemarks());

        // 계좌 잔액 확인
        Account updatedFromAccount = accountRepository.findByAccountNumber("111-222-3331").orElseThrow(() -> new NullPointerException("출금 계좌번호가 존재하지 않습니다."));
        Account updatedToAccount = accountRepository.findByAccountNumber("111-222-3332").orElseThrow(() -> new NullPointerException("수취 계좌번호가 존재하지 않습니다."));

        assertEquals(initialFromBalance, updatedFromAccount.getBalance());
        assertEquals(initialToBalance, updatedToAccount.getBalance());

        // 데이터베이스에 실패한 거래 기록이 저장되었는지 확인
        // Transaction savedTransaction = transactionRepository.findById(result.getId()).orElse(null);
        // assertNotNull(savedTransaction);
        // assertEquals(TransactionStatus.FAILURE, savedTransaction.getStatus());
        // assertEquals("잔액부족", savedTransaction.getRemarks());
    }

    @Test
    public void checkTransferFromSavingAccountTest() throws Exception {
        Account savingsAccount = accountRepository.findById(8L).orElseThrow(() ->
                new IllegalArgumentException("8번 계좌가 존재하지 않습니다."));
        // 일반 예금 계좌 설정 (id = 1)
        Account targetAccount = accountRepository.findById(1L).orElseThrow(() ->
                new IllegalArgumentException("Account with id 1 not found"));

        BigDecimal transferAmount = BigDecimal.valueOf(5000);


        BigDecimal initialSavingsBalance = savingsAccount.getBalance();
        BigDecimal initialTargetBalance = targetAccount.getBalance();

        // 이체 시도
        Assertions.assertThatThrownBy(() -> {
            transferService.transfer(savingsAccount.getAccountNumber(), targetAccount.getAccountNumber(),
                transferAmount);
        }).isInstanceOf(TransferErrorException.class).hasMessageContaining("cannot send from saving account");

        // 실패한 거래 기록 확인
        // assertNotNull(result);
        // assertEquals(TransactionStatus.FAILURE, result.getStatus());
        // assertEquals(savingsAccount, result.getAccount());
        // assertEquals(targetAccount, result.getSubAccount());
        // assertEquals(transferAmount, result.getAmount());
        // assertEquals(TransactionType.WITHDRAW, result.getType());

        // 송금이 실패했으므로 잔액이 변경되지 않았는지 검증
        Account updatedSavingsAccount = accountRepository.findById(8L).orElseThrow();
        Account updatedTargetAccount = accountRepository.findById(1L).orElseThrow();

        assertEquals(initialSavingsBalance, updatedSavingsAccount.getBalance());
        assertEquals(initialTargetBalance, updatedTargetAccount.getBalance());

        // 실패한 거래 기록이 데이터베이스에 저장되었는지 확인
        // Transaction savedTransaction = transactionRepository.findById(result.getId()).orElse(null);
        // assertNotNull(savedTransaction);
        // assertEquals(TransactionStatus.FAILURE, savedTransaction.getStatus());
        // assertEquals("적금 계좌 송금 불가", savedTransaction.getRemarks());
    }

    @Test
    @DisplayName("존재하지 않는 계좌번호로 이체 시도시 AccountNotFoundException 발생")
    public void transferWithNonExistentAccountTest() {
        // given
        String nonExistentAccountNumber = "999"; // 존재하지 않는 계좌번호
        String toAccountNumber = "1";
        BigDecimal amount = BigDecimal.valueOf(1000);

        // when & then
        Exception exception = assertThrows(AccountNotFoundException.class, () -> {
            transferService.transfer(nonExistentAccountNumber, toAccountNumber, amount);
        });

        // 에러 메시지 검증
        assertEquals("출금 계좌번호가 존재하지 않습니다.", exception.getMessage());

        // 실패 거래 기록 검증
        List<Transaction> failedTransactions = transactionRepository
                .findByTypeAndStatus(TransactionType.WITHDRAW, TransactionStatus.FAILURE);

        assertFalse(failedTransactions.isEmpty());
        Transaction failedTransaction = failedTransactions.get(failedTransactions.size() - 1);
        assertEquals(TransactionStatus.FAILURE, failedTransaction.getStatus());
        assertEquals(TransactionType.WITHDRAW, failedTransaction.getType());
        assertNotNull(failedTransaction.getRemarks());
    }

}
