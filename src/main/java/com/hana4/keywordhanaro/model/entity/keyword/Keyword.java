package com.hana4.keywordhanaro.model.entity.keyword;

import com.hana4.keywordhanaro.model.entity.account.Account;
import com.hana4.keywordhanaro.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "fk_Keyword_userId_User"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private KeywordType type;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "isFavorite", nullable = false)
    private boolean isFavorite = false;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "seqOrder", nullable = false)
    private Long seqOrder;

    private String inquiryWord;

    private Boolean checkEveryTime;

    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(columnDefinition = "JSON")
    private String groupMember;

    @Column(columnDefinition = "JSON")
    private String branch;

    @ManyToOne
    @JoinColumn(name = "accountId", foreignKey = @ForeignKey(name = "fk_Keyword_accountId_Account"))
    private Account account;

    @ManyToOne
    @JoinColumn(name = "subAccountId", foreignKey = @ForeignKey(name = "fk_Keyword_subAccountId_Account"))
    private Account subAccount;

    public Keyword(User user, KeywordType type, Long seqOrder, String name, boolean isFavorite, String branch,
                   String description) {
        this.user = user;
        this.type = type;
        this.seqOrder = seqOrder;
        this.name = name;
        this.isFavorite = isFavorite;
        this.branch = branch;
        this.description = description;
    }
}
