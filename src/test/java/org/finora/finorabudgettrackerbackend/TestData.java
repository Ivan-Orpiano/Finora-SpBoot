package org.finora.finorabudgettrackerbackend;

import java.math.BigDecimal;
import  java.time.LocalDate;

public class TestData {

    private TestData(){
        //utility class in order to prevent instantiation
    }

    static BigDecimal amount(String value) {
        return new BigDecimal(value);
    }

    static User user(Long id) {
        User user = new User();

        //data samples
        user.id = id;
        user.name = "Test User";
        user.email = "ivanritchel.crypto@gmail.com";
        user.passwordHash = "@!SampleHashForTests";
        user.preferredCurrency = "PHP";
        user.alertThreshold= 80;

        return  user;
    }

    static Account account(Long id, Long ownerId) {
        Account account = new Account();

        account.id=id;
        account.ownerId = ownerId;
        account.name = "Saving Account";
        account.type = AccountType.SAVINGS;
        account.openiningBalance = amount("10000.00");
        account.currency= "PHP";

        return account;
    }

    static  Transaction transaction(
            Long id,
            Long ownerId,
            Long accountId,
            TransactionType type,
            String amount
    ){
        Transaction transaction = new Transaction();

        transaction.id = id;
        transaction.ownerId = ownerId;
        transaction.accountId = accountId;
        transaction.type = type;
        transaction.status = TransactionStatus.COMPLETED;
        transaction.category = type == TransactionType.INCOME ? "Salary" : "Food";
        transaction.description = "Test description";
        transaction.date = LocalDate.now();
        transaction.amount = amount(amount);

        return transaction;
    }



}
