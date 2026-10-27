package org.finora.finorabudgettrackerbackend.transaction;

import org.finora.finorabudgettrackerbackend.account.AccountRepository;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.*;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactions;
    private final AccountRepository accounts;

    public TransactionService(
            TransactionRepository transactions,
            AccountRepository accounts)
    {
        this.transactions = transactions;
        this.accounts = accounts;
    }

    //input DTO for creating or updating transactions
    public record Request(
            @NotNull Long accountId,
            @NotNull TransactionType type,
            TransactionStatus status,
            @NotBlank String category,
            String description,
            @NotNull LocalDate date,
            @NotNull
            @DecimalMin("0.01")
            @Digits(integer = 17, fraction = 2)
            BigDecimal amount
    ){}

    //output DTO return to the frontend
    public record View(
            Long id,
            Long accountId,
            TransactionType type,
            TransactionStatus status,
            String category,
            String description,
            LocalDate date,
            BigDecimal amount) {}

    //converts an entity to a safe response
    public static View toView(Transaction tx) {
        return new View(
                tx.getId(),
                tx.getAccountId(),
                tx.getType(),
                tx.getStatus(),
                tx.getCategory(),
                tx.getDescription(),
                tx.getDate(),
                tx.getAmount()
        );
    }

    //handles both (create) and PUT (update) operations
    public View save(Long userId, Long id, Request request) {
        //the target account must belong to the current user
        if(accounts.findByIdAndOwnerId(
                request.accountId(), userId).isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Account not found"
            );
        }
        //validate the amount even when the service is called directly
        if(request.amount().signum() <= 0 || request.amount().scale() > 2) {

            throw new IllegalArgumentException("Amount must be positive with at most two decimals");
        }

        TransactionStatus status = request.status() == null
                ? TransactionStatus.COMPLETED
                : request.status();

        String description = request.description() == null
                ? ""
                : request.description().trim();

        Transaction tx;

        if(id == null) {
            tx = new Transaction(
                    userId,
                    request.accountId(),
                    request.type(),
                    status,
                    request.category().trim(),
                    description,
                    request.date(),
                    request.amount(),
                    null
            );
        } else {
            tx = transactions.findByIdAndOwnerId(id, userId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Transaction not found"
                    ));


            tx.update(
                    request.accountId(),
                    request.type(),
                    status,
                    request.category().trim(),
                    description,
                    request.date(),
                    request.amount()
            );
        }
        return toView(transactions.save(tx));
    }

    public void delete(Long userId, Long id) {
        Transaction tx = transactions
                .findByIdAndOwnerId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Transaction not found"
                ));
        transactions.delete(tx);
    }

    //dynamic search with user isolation, sorting, and pagination
    @Transactional(readOnly = true)
    public Page<View> search (
       Long userId,
       LocalDate start,
       LocalDate end,
       TransactionType type,
       TransactionStatus status,
       Long accountId,
       String category,
       String search,
       int page,
       int size,
       String sort,
       String direction)
    {
        if(start != null && end !=  null && start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }


        Specification<Transaction> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            //always restrict queries to the authenticated ser
            predicates.add();
        }
    }

}
