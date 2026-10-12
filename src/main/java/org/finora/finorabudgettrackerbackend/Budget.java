package org.finora.finorabudgettrackerbackend;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "budgets",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"owner_id", "budget_month", "category"}
        )
)
public class Budget {


}
