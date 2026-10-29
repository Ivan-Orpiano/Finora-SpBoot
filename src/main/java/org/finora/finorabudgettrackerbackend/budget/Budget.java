package org.finora.finorabudgettrackerbackend.budget;

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
    private Long id;


    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "budget_month", nullable = false, length = 7)
    private String month;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal limitAmount;

    protected Budget() {
    }


    public Budget(
            Long ownerId,
            String month,
            String category,
            BigDecimal limitAmount
    ) {
        this.ownerId = ownerId;
        this.month = month;
        update(category, limitAmount);
    }

    public void update(String category, BigDecimal limitAmount) {
        this.category = category;
        this.limitAmount = limitAmount;
    }


    public Long getId() {return id;}
    public Long getOwnerId(){return ownerId;}
    public String getMonth(){return  month;}
    public String getCategory() {return category;}
    public BigDecimal getLimitAmount(){return limitAmount;}
}