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