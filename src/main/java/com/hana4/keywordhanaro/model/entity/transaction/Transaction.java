package com.hana4.keywordhanaro.model.entity.transaction;

import com.hana4.keywordhanaro.model.entity.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false, foreignKey = @ForeignKey(name = "fk_Transaction_accountId_Account"))
    private Account account;

    @ManyToOne
    @JoinColumn(name = "subAccountId", foreignKey = @ForeignKey(name = "fk_Transaction_subAccountId_Account"))
    private Account subAccount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(length = 36)
    private String alias;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal beforeBalance;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal afterBalance;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "timestamp")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    private String remarks;

    public Transaction(Account account, Account subAccount, BigDecimal amount, TransactionType type, String alias,
                       BigDecimal beforeBalance, BigDecimal afterBalance, TransactionStatus status, LocalDateTime createAt) {
        this.account = account;
        this.subAccount = subAccount;
        this.amount = amount;
        this.type = type;
        this.alias = alias;
        this.beforeBalance = beforeBalance;
        this.afterBalance = afterBalance;
        this.status = status;
        this.createAt = createAt;
    }
}
