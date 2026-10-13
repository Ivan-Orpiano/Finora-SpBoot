package org.finora.finorabudgettrackerbackend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//jpa specification

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmailContainingIgnoreCase(String email);
}

interface AccountRepo extends JpaRepository<Account, Long> {
    List<Account> findByOwnerIdOrderByNameAsc(Long ownerId);
    Optional<Account> findByAndOwnerId(Long id, Long ownerId);
    long  countByOwnerId(Long ownerId);
}

interface TransactionRepo extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    //for User ID
    Optional<Transaction> findByIdAndOwnerId(Long id, Long ownerId);


    //for Account ID
    List<Transaction> findyOwnerIdAndAccountId(
            Long ownerId, Long accountId
    );
    // between owner's status and owner id gap
    List<Transaction> findByOwnerIdAndDateBetweenAndStatus(
            Long ownerId,
            LocalDate start,
            LocalDate end,
            TransactionStatus status
    );

    List<Transaction> findByOwnerIdAndStatusAndDateLessThanEqual(
            Long ownerId,
            TransactionStatus status,
            LocalDate date
    );

    boolean existByAccountIdAndExternalKey(
      Long accountId, String externalKey
    );
    long countByOwnerIdAndAccountId(Long ownerId, Long accountId);
}

interface BudgetRepo extends JpaRepository<Budget, Long> {
    List<Budget> findByOwnerIdAndMonth(Long ownerId, String month);

    Optional <Budget> findByOwnerIdAndMonthCategoryIgnoreCase(
            Long ownerId,
            String month,
            String catergory
    );
}

interface GoalRepo extends JpaRepository<Goal, Long> {
    List<Goal> findByOwnerIdOrderByIdDesc (Long ownerId);
    Optional<Goal> findByIdAndOwnerId(Long id, Long ownerId);
}

interface RecurringRepo extends JpaRepository<RecurringRule, Long> {

}

