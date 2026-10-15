package org.finora.finorabudgettrackerbackend;

import jakarta.validation.constraints.*;
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