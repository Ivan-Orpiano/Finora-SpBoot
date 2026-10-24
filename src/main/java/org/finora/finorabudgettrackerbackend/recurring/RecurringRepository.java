package org.finora.finorabudgettrackerbackend.recurring;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecurringRepository
    extends JpaRepository<RecurringRule, Long>{

    //find a user's recurring rules
    List<RecurringRule> findByOwnerId(Long ownerId);

    //find one recurring rule by both ID and owner
    Optional<RecurringRule> findByIdAndOwnerId(Long id, Long ownerId);

    //find active recurring rules that are due
    List<RecurringRule> findByActiveTrueAndNextDueDateLessThanEqual(
      LocalDate date
    );

    //check if an account has recurring rules
    long countByOwnerIdAndAccountId(
            Long ownerId, Long accountId
    );
}
