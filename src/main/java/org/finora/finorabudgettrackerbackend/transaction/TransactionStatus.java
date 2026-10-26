package org.finora.finorabudgettrackerbackend.transaction;

// Describes the transaction's current state.
public enum TransactionStatus {
    PENDING,
    COMPLETED,
    CANCELED
}
