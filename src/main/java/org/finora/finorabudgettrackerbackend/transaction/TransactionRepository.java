package org.finora.finorabudgettrackerbackend.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends
        JpaRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {

    //find a transaction owned by the authenticated user
    Optional<Transaction> findByIdAndOwnerId(
            Long id, Long ownerId
    );

    //find transactions affecting a specific account
    List<Transaction> findByOwnerIdAndAccountId(
            Long ownerId, Long accountId
    );

    //Used for dashboard totals and budget calculations
    List<Transaction> findByOwnerIdAndDateBetweenAndStatus(
            Long ownerId,
            LocalDate start,
            LocalDate end,
            TransactionStatus status
    );

    //check whether an imported transaction already exists
    boolean existsByAccountIdAndExternalKey(
      Long accountId, String externalKey
    );

    //used before allowing account deletion
    long countByOwnerIdAndAccountId(
            Long ownerId, Long accountId
    );
}
