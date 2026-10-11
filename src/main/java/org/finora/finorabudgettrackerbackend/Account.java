package org.finora.finorabudgettrackerbackend;

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
    AccessType type;

    //opening balance is the starting amount in te account.

    @Column(nullable = false, precision = 19, scale = 2)
    BigDecimal openiningBalance = BigDecimal.ZERO;

    @Column(nullable = false, length = 3)
    String currency = "PHP";

    Instant lastSyncedAt;

    String sysncStatus = "NEVER";

    @Column(length = 1000)
    String syncError;

    protected Account() {}


}
