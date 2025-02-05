/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.users.txoutbox;

import io.oigres.ecomm.service.users.domain.TransactionalOutbox;
import io.oigres.ecomm.service.users.repository.TransactionalOutboxRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageRelayService {
  private final TransactionalOutboxRepository transactionalOutboxRepository;
  private final KafkaTemplate<String, TransactionalOutbox> kafkaTemplate;

  public MessageRelayService(
      TransactionalOutboxRepository transactionalOutboxRepository,
      KafkaTemplate<String, TransactionalOutbox> kafkaTemplate) {
    this.transactionalOutboxRepository = transactionalOutboxRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Transactional
  public void processTransactionalOutboxes() {
    List<TransactionalOutbox> outboxes =
        this.transactionalOutboxRepository.findAndLockTopNByIsDelivered(false, Pageable.ofSize(10));
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
    List<TransactionalOutbox> outboxes =
        this.transactionalOutboxRepository.findAndLockTopNByIsDelivered(true, Pageable.ofSize(10));
    if (!outboxes.isEmpty()) {
      this.transactionalOutboxRepository.deleteAllInBatch(outboxes);
    }
  }
}
