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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.oigres.ecomm.service.users.api.txoutbox.UserOutbox;
import io.oigres.ecomm.service.users.config.mapper.ResponsesMapper;
import io.oigres.ecomm.service.users.domain.Aggregate;
import io.oigres.ecomm.service.users.domain.TransactionalOutbox;
import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.repository.TransactionalOutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionalOutboxPatternEntityListener
    implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

  private final TransactionalOutboxRepository transactionalOutboxRepository;
  private final ResponsesMapper modelMapper;
  private final ObjectMapper json;

  public TransactionalOutboxPatternEntityListener(
      TransactionalOutboxRepository transactionalOutboxRepository, ResponsesMapper modelMapper) {
    this.transactionalOutboxRepository = transactionalOutboxRepository;
    this.modelMapper = modelMapper;
    this.json = new ObjectMapper();
    this.json.registerModule(new JavaTimeModule());
    this.json.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Override
  public boolean requiresPostCommitHandling(EntityPersister persister) {
    return false;
  }

  @Override
  public void onPostInsert(PostInsertEvent event) {
    Object entity = event.getEntity();
    if (entity == null) {
      return;
    }
    if (User.class.isAssignableFrom(entity.getClass())) {
      saveEntityStateChangedEvent((User) entity, Aggregate.USER);
    }
  }

  @Override
  public void onPostUpdate(PostUpdateEvent event) {
    Object entity = event.getEntity();
    if (entity == null) {
      return;
    }
    if (User.class.isAssignableFrom(entity.getClass())) {
      saveEntityStateChangedEvent((User) entity, Aggregate.USER);
    }
  }

  @Override
  public void onPostDelete(PostDeleteEvent event) {}

  private void saveEntityStateChangedEvent(User user, Aggregate aggregate) {
    try {
      UserOutbox outbox = this.modelMapper.toUserOutbox(user);
      TransactionalOutbox txOutbox =
          TransactionalOutbox.builder()
              .aggregate(aggregate)
              .delivered(Boolean.FALSE)
              .message(this.json.writeValueAsString(outbox))
              .build();
      this.transactionalOutboxRepository.save(txOutbox);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
