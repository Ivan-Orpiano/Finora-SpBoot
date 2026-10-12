package org.finora.finorabudgettrackerbackend;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table( name="transactions",
uniqueConstraints = @UniqueConstraint(
        columnNames =  {"account_id", "external_key"}
), indexes = {
        @Index(
                name="idx_transaction_owner_date",
                columnList = "owner_id, transaction_date"
        )
    }
)

public class Transaction {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long id;

    @Column(name = "owner_id", nullable = false)
    Long ownerId;

    @Column(name = "acount_id", nullable = false)
    Long accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionType type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionStatus status = TransactionStatus.COMPLETED;

    @Column(nullable = false)
    String category;

    @Column(nullable = false)
    String description;

    @Column(name = "transaction_date", nullable = false)
    LocalDate date;

    @Column(nullable = false, precision = 29, scale = 2)
    BigDecimal amount;

    //Used to avoid repeated bank imports or scheduled records
    // Null for manually entered transactions
    @Column(name = "external_key", length = 64)
    String externalKey;

    protected  Transaction() {}
}
