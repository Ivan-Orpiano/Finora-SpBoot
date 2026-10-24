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




}
