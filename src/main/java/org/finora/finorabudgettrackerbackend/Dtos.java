package org.finora.finorabudgettrackerbackend;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Map;

record  RegisterIn(
   @NotBlank String name,
   @Email @NotBlank String email,
   @Size(min = 8) String password
) {}