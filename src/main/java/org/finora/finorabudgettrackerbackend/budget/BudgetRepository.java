package org.finora.finorabudgettrackerbackend.budget;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long>{


    //Retrieve a user's budgets for one month
    List<Budget> findByOwnerIdAndMonth(
            Long ownerId, String month
    );

    //find an existing budget before creating a duplicate
    Optional<Budget> findByOwnerIdAndMonthAndCategoryIgnoreCase(
      Long ownerId, String month, String category
    );
}
