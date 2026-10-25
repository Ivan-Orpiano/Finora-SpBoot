package org.finora.finorabudgettrackerbackend.transaction;

import jakarta.persistence.*;
import org.finora.finorabudgettrackerbackend.transaction.TransactionStatus;
import org.finora.finorabudgettrackerbackend.transaction.TransactionType;

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

    public Transaction(
        Long ownerId,
        Long accountId,
        TransactionType type,
        TransactionStatus status,
        String category,
        String description,
        LocalDate date,
        BigDecimal amount,
        String externalKey
    ){
        this.ownerId = ownerId;
        this.externalKey = externalKey;

        update(
                accountId,
                type,
                status,
                category,
                description,
                date,
                amount
        );

    }

    //changes transaction details without changing ownership
    public void update(
            Long accountId,
            TransactionType type,
            TransactionStatus status,
            String category,
            String description,
            LocalDate date,
            BigDecimal amount
    ) {
        this.accountId = accountId;
        this.type = type;
        this.status = status;
        this.category = category;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    public Long getId(){return id;}
    public Long getOwnerId(){return  ownerId;}
    public Long getAccountId(){return accountId;}
    public TransactionType getType() {return type;}
    public TransactionStatus getStatus() {return status;}
    public String getCategory(){return  category;}
    public String getDescription() {return description;}
    public LocalDate getDate(){return  date;}
    public BigDecimal getAmount() {return amount;}
    public String getExternalKey(){return externalKey;}

}
