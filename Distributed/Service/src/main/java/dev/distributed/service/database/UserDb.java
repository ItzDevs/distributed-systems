package dev.distributed.service.database;

import dev.distributed.service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface UserDb extends JpaRepository<User, UUID> {
    /**
     * Save or update a User
     *
     * @param user The Record to save
     * @return The saved record
     */
    @Retryable(backoff = @Backoff(2000))
    @Modifying
    @Transactional
    <S extends User> S save(S user);
}

