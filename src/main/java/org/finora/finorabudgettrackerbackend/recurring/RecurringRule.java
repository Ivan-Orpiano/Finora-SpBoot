package org.finora.finorabudgettrackerbackend.recurring;

import jakarta.persistence.*;
import org.finora.finorabudgettrackerbackend.Recurrence;
import org.finora.finorabudgettrackerbackend.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "recurring_rules")
public class RecurringRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long ownerId;

    @Column(nullable = false)
    Long accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionType type;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Recurrence frequency;

    @Column(nullable = false)
    String category;

    @Column(nullable = false)
    String description;

    @Column(nullable = false, precision = 19, scale = 2)
    BigDecimal amount;

    @Column(nullable = false)
    LocalDate nextDueDate;

    @Column(nullable = false)
    boolean active = true;

    protected RecurringRule() {}




}
