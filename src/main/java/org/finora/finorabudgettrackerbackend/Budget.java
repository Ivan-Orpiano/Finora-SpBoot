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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Column(name = "owner_id", nullable = false)
    Long ownerId;

    @Column(name = "budget_month", nullable = false, length =7)
    String month;

    @Column(nullable = false)
    String category;

    @Column(nullable = false, precision = 19,scale = 2)
    BigDecimal limitAmount;

    protected Budget(){}
}
