package org.finora.finorabudgettrackerbackend;

enum AccountType {
    CHECKING,
    SAVINGS,
    CASH,
    CREDIT_CARD,
    DIGITAL_WALLET
}

enum TransactionType {
    INCOME,
    EXPENSE
}

enum TransactionStatus {
    PENDING,
    COMPLETED,
    CANCELED
}
enum Recurrence {
    DAILY,
    WEEKLY,
    MONTHLY
}