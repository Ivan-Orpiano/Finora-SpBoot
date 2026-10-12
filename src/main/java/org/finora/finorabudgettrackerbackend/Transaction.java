package org.finora.finorabudgettrackerbackend;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table( name="transactions",
uniqueConstraints = @UniqueConstraint(
        columnNames =  {"account_id", "external_key"}
), indexes = {
        @Index(
                name="idx_transaction_owner_date",
                columnList = "owner_id, transaction_date"
        )
    }
)

public class Transaction {



















}
