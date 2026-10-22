package org.finora.finorabudgettrackerbackend.account;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long ownerId;

    @Column(nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AccountType type;

    //opening balance is the starting amount in te account.

    @Column(nullable = false, precision = 19, scale = 2)
    BigDecimal openingBalance = BigDecimal.ZERO;

    @Column(nullable = false, length = 3)
    String currency = "PHP";

    Instant lastSyncedAt;

    String sysncStatus = "NEVER";

    @Column(length = 1000)
    String syncError;

    protected Account() {}

    public Account(
            Long ownerId, String name, AccountType type,
            BigDecimal openingBalance,
            String currency
    ) {
        this.ownerId = ownerId;
        update(name,type, openingBalance);
        this.currency = currency;
    }

    public void update(
            String name, AccountType   type, BigDecimal openingBalance
    ) {
        this.name = name;
        this.type = type;
        this.openingBalance = openingBalance;
    }

    public void updateSync(String status, String error) {
        this.sysncStatus = status;
        this.syncError = error;
        this.lastSyncedAt = Instant.now();
    }

    public Long getId() { return id;}
    public Long getOwnerId() {return ownerId;}
    public String getName() {return name;}
    public AccountType getType(){return type;}
    public BigDecimal getOpeningBalance() { return  openingBalance;}
    public String getCurrency() {return  currency;}
    public Instant getLastSyncedAt(){return lastSyncedAt;}
    public String getSysncStatus() { return sysncStatus;}
    public String getSyncError() {return syncError;}
}
