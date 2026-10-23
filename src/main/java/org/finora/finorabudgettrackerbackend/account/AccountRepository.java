package org.finora.finorabudgettrackerbackend.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

public interface AccountRepository extends  JpaRepository<Account, Long> {

    List<Account> findByOwnerIdOrderByNameAsc(Long ownerId);

    Optional<Account> findByIdAndOwnerId(Long id, Long ownerId);

    long countByOwnerId(Long ownerId);
}
