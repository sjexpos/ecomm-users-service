package io.oigres.ecomm.service.users.config;

import io.oigres.ecomm.service.users.txoutbox.TransactionalOutboxPatternEntityListener;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TransactionalOutboxEntityListenerRegister {

    private final SessionFactory sessionFactory;
    private final TransactionalOutboxPatternEntityListener listener;

    public TransactionalOutboxEntityListenerRegister(SessionFactory sessionFactory, TransactionalOutboxPatternEntityListener listener) {
        this.sessionFactory = sessionFactory;
        this.listener = listener;
    }

    @PostConstruct
    public void registerListeners() {
        SessionFactoryImpl sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        ServiceRegistryImplementor serviceRegistry = sessionFactoryImpl.getServiceRegistry();
        EventListenerRegistry registry = serviceRegistry.getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(listener);
        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(listener);
        registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(listener);
    }

}
