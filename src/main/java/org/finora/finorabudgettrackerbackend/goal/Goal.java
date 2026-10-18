package org.finora.finorabudgettrackerbackend.goal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long ownerId;

    @Column(nullable = false)
    String name;

    @Column(nullable = false, precision = 19, scale = 2)
    BigDecimal targetAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    BigDecimal savedAmount = BigDecimal.ZERO;

    LocalDate deadline;

    protected Goal(){}
}
