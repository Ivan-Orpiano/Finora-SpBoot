package org.finora.finorabudgettrackerbackend.transaction;

enum TransactionType {
    INCOME,
    EXPENSE;

    enum TransactionStatus {
        PENDING,
        COMPLETED,
        CANCELED
    }
}
