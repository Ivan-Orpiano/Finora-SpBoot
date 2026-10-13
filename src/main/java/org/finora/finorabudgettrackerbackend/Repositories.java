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

