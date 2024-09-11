package io.oigres.ecomm.service.users.txoutbox;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.oigres.ecomm.service.users.domain.TransactionalOutbox;
import io.oigres.ecomm.service.users.repository.TransactionalOutboxRepository;
import jakarta.transaction.Transactional;

@Service
public class MessageRelayService {
    private final TransactionalOutboxRepository transactionalOutboxRepository;
    private final KafkaTemplate<String, TransactionalOutbox> kafkaTemplate;

    public MessageRelayService(
        TransactionalOutboxRepository transactionalOutboxRepository, 
        KafkaTemplate<String, TransactionalOutbox> kafkaTemplate
    ) {
        this.transactionalOutboxRepository = transactionalOutboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void processTransactionalOutboxes() {
        List<TransactionalOutbox> outboxes = this.transactionalOutboxRepository.findAndLockTopNByIsDelivered(false, Pageable.ofSize(10));
        for (TransactionalOutbox outbox : outboxes) {
            if (!outbox.getDelivered()) {
                this.kafkaTemplate.sendDefault(outbox);
                outbox.setDelivered(true);
                this.transactionalOutboxRepository.save(outbox);
            }
        }
    }

    @Transactional
    public void cleanDeliveredTransactionalOutboxes() {
        List<TransactionalOutbox> outboxes = this.transactionalOutboxRepository.findAndLockTopNByIsDelivered(true, Pageable.ofSize(10));
        if (!outboxes.isEmpty()) {
            this.transactionalOutboxRepository.deleteAllInBatch(outboxes);
        }
    }

}
