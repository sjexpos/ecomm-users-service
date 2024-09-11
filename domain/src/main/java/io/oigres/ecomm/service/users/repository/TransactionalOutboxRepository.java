package io.oigres.ecomm.service.users.repository;

import java.util.List;

import io.oigres.ecomm.service.users.domain.TransactionalOutbox;
import org.springframework.data.domain.Pageable;

public interface TransactionalOutboxRepository {

    TransactionalOutbox save(TransactionalOutbox user);

    List<TransactionalOutbox> findAndLockTopNByIsDelivered(boolean state, Pageable pageable);

    void deleteAllInBatch(Iterable<TransactionalOutbox> entities);

}
