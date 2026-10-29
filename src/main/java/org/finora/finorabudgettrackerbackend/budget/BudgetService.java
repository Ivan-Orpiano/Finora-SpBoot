package org.finora.finorabudgettrackerbackend.budget;

import org.finora.finorabudgettrackerbackend.user.*;
import org.finora.finorabudgettrackerbackend.transaction.*;

import jakarta.validation.constraints.*;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;


@Service
@Transactional
public class BudgetService {

    private final BudgetRepository budgets;
    private final TransactionRepository transactions;
    private  final UserRepository users;

    public BudgetService(
            BudgetRepository budgets,
            TransactionRepository transactions,
            UserRepository users
    )
    {
        this.budgets = budgets;
        this.transactions = transactions;
        this.users = users;
    }

    public record Request(
            @NotBlank String month,
            @NotBlank String category,
            @NotNull
            @DecimalMin("0.01")
            @Digits(integer =17, fraction = 2)
            BigDecimal limitAmount ) { }

    public record View(
            Long id,
            String category,
            BigDecimal limitAmount,
            BigDecimal spent,
            BigDecimal remaining,
            double percentUsed,
            boolean alert
    ){}

    //create or update a monthly budget for a category
    public View save(Long userId, Request request) {

        YearMonth.parse(request.month());

        if(request.limitAmount().signum() <= 0
               || request.limitAmount().scale() >2) {
            throw new IllegalArgumentException(
                    "Invalid monthly budget amount"
            );
        }

        String category = request.category().trim();

        Budget budget =
                budgets.findByOwnerIdAndMonthAndCategoryIgnoreCase(
                userId,
                request.month(),
                category
        ).orElseGet(() -> new Budget(
                userId,
                request.month(),
                category,
                request.limitAmount()
                ));

        budget.update(category, request.limitAmount());

        return toView(userId, budgets.save(budget));
    }

    // calculate spending, remaining amount, and budget utilization
    public View toView(Long userId, Budget budget) {
        YearMonth month = YearMonth.parse(budget.getMonth());

        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        //only count current or past completed expenses
        LocalDate effectiveEnd = end.isAfter(LocalDate.now())
            ? LocalDate.now() : end;

        List <Transaction> periodTransactions = effectiveEnd.isBefore(start)
                ? List.of()
                : transactions.findByOwnerIdAndDateBetweenAndStatus(
                    userId,
                    start,
                    effectiveEnd,
                    TransactionStatus.COMPLETED
        );


        BigDecimal spent = periodTransactions.stream().filter(tx -> tx.getType() == TransactionType.EXPENSE)
                .filter(tx -> budg)



        return pass
    }
}
