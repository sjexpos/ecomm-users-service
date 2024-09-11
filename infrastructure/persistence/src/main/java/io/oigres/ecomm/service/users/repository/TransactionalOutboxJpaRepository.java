package io.oigres.ecomm.service.users.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.oigres.ecomm.service.users.domain.TransactionalOutbox;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

@Repository
public interface TransactionalOutboxJpaRepository extends TransactionalOutboxRepository, JpaRepository<TransactionalOutbox, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    @Query(value = "SELECT o FROM TransactionalOutbox o WHERE o.delivered = :state")
    List<TransactionalOutbox> findAndLockTopNByIsDelivered(@Param("state") boolean state, Pageable pageable);

}
