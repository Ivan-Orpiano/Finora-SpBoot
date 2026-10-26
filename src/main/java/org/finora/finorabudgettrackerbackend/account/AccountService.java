package org.finora.finorabudgettrackerbackend.account;

import org.finora.finorabudgettrackerbackend.user.*;
import org.finora.finorabudgettrackerbackend.transaction.*;
import org.finora.finorabudgettrackerbackend.recurring.RecurringRepository;


import jakarta.validation.constraints.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accounts;
    private final UserRepository users;
    private final TransactionRepository transactions;
    private final RecurringRepository recurring;

    public AccountService(
            AccountRepository accounts,
            UserRepository users,
            TransactionRepository transactions,
            RecurringRepository recurring
    ){
        this.accounts = accounts;
        this.users = users;
        this.transactions = transactions;
        this.recurring = recurring;
    }

    //information received when adding or updating an account
    public record Request(
            @NotBlank String name,
            @NotNull AccountType type,
            @NotNull
            @Digits(integer = 17, fraction = 2)
            BigDecimal openingBalance
    ){}

    // infromation returned to the front-end
    public record View(
       Long id,
       String name,
       AccountType type,
       BigDecimal openingBalance,
       BigDecimal currentBalance,
       String currency,
       Instant lastSyncedAt,
       String syncStatus,
       String syncError
    ) {}

    //central ownership check for account operations
    public Account requireOwned(Long userId, Long accountId){
        return accounts.findByIdAndOwnerId(accountId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Access not found"
                ));
    }

    //calculate the current balance from completed transaction

    public View toView(Account account){
        BigDecimal movement = transactions
                .findByOwnerIdAndAccountId(
                        account.getOwnerId(),
                        account.getId()
                )
                .stream()
                .filter(tx ->
                        tx.getStatus() == TransactionStatus.COMPLETED
                )
                .filter(tx ->
                        !tx.getDate().isAfter(LocalDate.now())
                )
                .map(tx ->
                        tx.getType() == TransactionType.INCOME
                ? tx.getAmount() : tx.getAmount().negate()
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal currentBalance = account.getOpeningBalance()
                .add(movement);

        return  new View(
                account.getId(),
                account.getName(),
                account.getType(),
                account.getOpeningBalance(),
                currentBalance,
                account.getCurrency(),
                account.getLastSyncedAt(),
                account.getSyncStatus(),
                account.getSyncError()
        );
    }

    @Transactional(readOnly = true)
    public List<View> list(Long userId) {
        return accounts.findByOwnerIdOrderByNameAsc(userId)
                .stream()
                .map(this::toView)
                .toList();
    }

    //create account or update an existing owned account
    public View save(Long userId, Long id, Request request){
        Account account;

        if(id == null) {
            User user = users.findById(userId)
                    .orElseThrow(()-> new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "User not found"
                    ));

            account = new Account(
              userId,
                    request.name().trim(),
                    request.type(),
                    request.openingBalance(),
                    user.getPreferredCurrency()
            );
        } else {
            account = requireOwned(userId, id);

            account.update(
                    request.name().trim(),
                    request.type(),
                    request.openingBalance()
            );
        }
        return  toView(accounts.save(account));
    }


    //prevent deletion when related financial data exists
    public void delete (Long userId, Long id) {
        Account account = requireOwned(userId, id);

        if(transactions.countByOwnerIdAndAccountId(userId, id) > 0 || recurring.countByOwnerIdAndAccountId(userId, id) > 0){
            throw new IllegalArgumentException(
                    "Delete related transactions and recurring rules firs"
            );
        }
        accounts.delete(account);
    }
}
