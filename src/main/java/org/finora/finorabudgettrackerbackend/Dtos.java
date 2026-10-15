package org.finora.finorabudgettrackerbackend;

import jakarta.validation.constraints.*;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Map;


// For connection in frontend
//User Register
record  RegisterIn(
   @NotBlank String name,
   @Email @NotBlank String email,
   @Size(min = 8) String password
) {}


//user log in
record LogIn(
        @Email @NotBlank String email,
        @NotBlank String password
) {}

record ProfileIn(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String preferredCurrency,
        @Min(1) @Max(100) int alertThreshold
){}

record ProfileView(
        Long id,
        String name,
        String email,
        String preferredCurrency,
        int alerThreshold
) {}

record AuthView(
        String token,
        ProfileView profiel
){}

record AccountIn(
   @NotBlank String name,
   @NotNull AccountType type,
   @NotNull BigDecimal openingBalance
) {}

record AccountView(
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

record TransactionIn(
        @NotNull Long accountId,
        @NotNull TransactionType type,
        TransactionStatus status,
        @NotBlank String category,
        String description,
        @NotNull LocalDate date,
        @NotNull @DecimalMin("0.01") BigDecimal amount
){}

record TransactionView(
        Long id,
        Long accountId,
        Transaction type,
        TransactionStatus status,
        String category,
        String description,
        LocalDate date,
        BigDecimal amount
){}

record BudgetIn(
        @NotBlank String month,
        @NotBlank String category,
        @NotNull @DecimalMin("0.01") BigDecimal limitAmount
){}

record BudgetView(
  Long id,
  String category,
  BigDecimal limitAmount,
  BigDecimal spent,
  BigDecimal remainnig,
  double percentUsed,
  boolean alert
){}

record GoalIn(
        @NotBlank String name,
        @NotNull @DecimalMin("0.01") BigDecimal targetAmount,
        @NotNull @DecimalMin("0.00") BigDecimal savedAmount,
        LocalDate deadline
){}

record GoalView(
        Long id,
        String name,
        BigDecimal targetAmount,
        BigDecimal savedAmount,
        LocalDate deadline,
        double percentComplete
){}

record RecurringIn(
        @NotNull Long accountId,
        @NotNull TransactionType type,
        @NotNull Recurrence frequency,
        @NotBlank String category,
        String description,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull LocalDate nextDueDate,
        boolean active
){}


record RecurringView(
        Long id,
        Long accountId,
        TransactionType type,
        Recurrence frequency,
        String category,
        String description,
        BigDecimal amount,
        LocalDate nextDueDate,
        boolean active
) {}

record ChartPoint(
        String month,
        BigDecimal income,
        BigDecimal expense
) {}

record DashboardView(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal netCashFlow,
        BigDecimal currentBalance,
        BigDecimal totalSavings,
        double savingsRate,
        Map<String, BigDecimal> spendingByCategory,
        List<ChartPoint> monthlyComparison,
        List<TransactionView> recentTransactions,
        List<BudgetView> budgets,
        List<RecurringView> upcomingBills,
        List<GoalView> goals
) {}