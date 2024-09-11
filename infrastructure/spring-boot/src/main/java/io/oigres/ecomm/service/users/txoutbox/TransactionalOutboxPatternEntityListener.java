package io.oigres.ecomm.service.users.txoutbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.oigres.ecomm.service.users.api.txoutbox.UserOutbox;
import io.oigres.ecomm.service.users.domain.Aggregate;
import io.oigres.ecomm.service.users.domain.TransactionalOutbox;
import io.oigres.ecomm.service.users.repository.TransactionalOutboxRepository;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

import org.modelmapper.ModelMapper;
import io.oigres.ecomm.service.users.domain.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TransactionalOutboxPatternEntityListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    private final TransactionalOutboxRepository transactionalOutboxRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper json;

    public TransactionalOutboxPatternEntityListener(TransactionalOutboxRepository transactionalOutboxRepository, ModelMapper modelMapper) {
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
            saveEntityStateChangedEvent(entity, Aggregate.USER, UserOutbox.class);
        }
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        Object entity = event.getEntity();
        if (entity == null) {
            return;
        }
        if (User.class.isAssignableFrom(entity.getClass())) {
            saveEntityStateChangedEvent(entity, Aggregate.USER, UserOutbox.class);
        }
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
    }

    private void saveEntityStateChangedEvent(Object object, Aggregate aggregate, Class<?> outboxClass) {
        try {
            Object outbox = this.modelMapper.map(object, outboxClass);
            TransactionalOutbox txOutbox = TransactionalOutbox.builder()
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
